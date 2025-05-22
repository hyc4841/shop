package love.shop.service.member;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.common.exception.UnauthorizedAccessException;
import love.shop.common.exception.UserNotExistException;
import love.shop.domain.address.Address;
import love.shop.domain.cart.Cart;
import love.shop.domain.member.Member;
import love.shop.domain.member.MemberRole;
import love.shop.domain.member.Role;
import love.shop.repository.address.AddressRepository;
import love.shop.repository.member.MemberRepository;
import love.shop.service.RedisService;
import love.shop.web.login.dto.MemberDto;
import love.shop.web.member.dto.AddressUpdateReqDto;
import love.shop.web.signup.dto.SignupRequestDto;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final RedisService redisService;
    private final AddressRepository addressRepository;

    @Transactional
    public Member addAddress(Integer zipcode, String city, String street, String detailedAddress, Member member, String addressName) {

        Address address = new Address(city, street, zipcode.toString(), detailedAddress, member, addressName);
        address.setMember(member);

        addressRepository.save(address);

        return member;
    }

    private void makeBindingError(BindingResult bindingResult, String field, String message) throws MethodArgumentNotValidException {
        bindingResult.rejectValue(field, null, message);
        throw new MethodArgumentNotValidException(null, bindingResult);
    }

    public void checkEmailCertification(String email, BindingResult bindingResult) throws MethodArgumentNotValidException {

        // 이메일 인증에 성공하면 redis 안에선 email - "ok" 형태로 있을 것임 그것을 확인
        // 이메일 인증 요청만 한 상태면 email - "sent"
        String certificationStatus = redisService.getValue(email);
        if (!Objects.equals(certificationStatus, "ok")) {
            makeBindingError(bindingResult, "email", "이메일 인증을 먼저 해주세요.");
        }

        // 인증 확인이 되면 해당 이메일 인증 상태를 삭제한다.
        redisService.removeData(email);
    }

    // 회원가입
    @Transactional
    public Member signUp(SignupRequestDto signupDto) {
        // 아이디 중복, 이메일 인증은 다른 메서드에서 진행
        // 비밀번호 암호화
        String password = passwordEncoder.encode(signupDto.getPasswordAndCheck().getPassword());

        // dto 엔티티로 변환
        Member member = signupDto.toMemberEntity(password);

        // 꼭 회원가입할 때 주소를 입력해야 하나?
        Address address = new Address(signupDto.getCity(), signupDto.getStreet(), signupDto.getZipcode(), signupDto.getDetailedAddress());
        address.setMember(member);

        MemberRole memberRole = new MemberRole(Role.MEMBER, member);
        memberRole.setMember(member);

        Cart cart = new Cart(member);

        // 양방향 연관관계는 무조건 양쪽 다 연결해주어야 한다.
        // 멤버 쪽
        memberRepository.save(member);

        return member;
    }

    // 이메일 인증코드 요청
    @Transactional
    public void sendEmailCertification(String email) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setFrom("hyc4841@gmail.com");
        message.setSubject("이메일 인증");

        String code = redisService.generateCode(email);
        log.info("이메일 인증 코드={}", code);
        message.setText(code); // 이메일 전송

        javaMailSender.send(message);
    }

    // 이메일 인증 코드 확인
    @Transactional
    public void confirmEmailCertification(String email, String code, BindingResult bindingResult) throws MethodArgumentNotValidException {

        if (Objects.equals(code, "")) {
            FieldError error = new FieldError(code, "code",
                    "인증 코드를 입력해주세요.");
            bindingResult.addError(error);
            throw new MethodArgumentNotValidException(null, bindingResult);
        }

        String matchingEmail = redisService.getValue(code); // 코드로 이메일 찾기
        String isSent = redisService.getValue(email);       // 인증 코드 이메일 전송 여부 확인

        if (isSent == null) {
            FieldError error = new FieldError("emailCertificationConfirmDto", "email",
                    "해당 이메일로 인증 코드가 전송된 적이 없습니다. 먼저 인증 요청을 해주세요");
            bindingResult.addError(error);
            throw new MethodArgumentNotValidException(null, bindingResult);
        }

        // 해당 코드의 이메일이 맞는지 확인
        if (Objects.equals(matchingEmail, email)) {
            // 인증 코드가 맞으면 redis에서 해당 인증 코드를 삭제
            redisService.removeData(code);
//            redisService.removeData(email); // 이과정이 필요가 있나? 밑에 그냥 해당 키로 데이터 저장하면 덮어 써지나?
            // 이메일 인증은 redis에 ok 처리하자 그냥. 그리고 이메일 인증 후 30분까지 가입을 완료하지 않으면 인증 기록 삭제. 즉 다시 해야함.
            redisService.saveDataWithExpire(email,"ok",  60 * 30L);
        } else {
            // 아니면 오류를 뱉는다.
            FieldError error = new FieldError("emailCertificationConfirmDto", "code", "잘못된 인증 코드입니다.");
            bindingResult.addError(error);
//            bindingResult.rejectValue("code", String.valueOf(HttpStatus.BAD_REQUEST), "잘못된 인증 코드입니다");

            throw new MethodArgumentNotValidException(null, bindingResult);
        }
    }

    public void loginIdDuplicationValidation(String loginId, BindingResult bindingResult) throws MethodArgumentNotValidException {
        Member member = memberRepository.findMemberByLoginId(loginId).orElse(null);
        if (member != null) {
            makeBindingError(bindingResult, "loginId", "이미 등록된 ID 입니다.");
        }
    }

    public MemberDto memberInfo(Long memberId) {
        Member member = findOne(memberId);
        return new MemberDto(member);
    }

    public List<Member> findAllMember() {
        return memberRepository.findAllMember();
    }

    public List<Member> findMemberByName(String memberId) {
        return memberRepository.findMemberByName(memberId);
    }

    public Optional<Member> findMemberByLoginId(String loginId) {
        return memberRepository.findMemberByLoginId(loginId);
    }

    // 멤버 pk로 조회
    public Member findMemberById(Long memberId) {
        return findOne(memberId);
    }

    public Optional<Member> findMemberByEmail(String email) {
        return memberRepository.findMemberByEmail(email);
    }

    public boolean curPasswordCheck(String curPwd, Long memberId) {
        Member member = findOne(memberId);
        return passwordEncoder.matches(curPwd, member.getPassword());
    }

    @Transactional
    public Member updatePassword(String newPassword, Long memberId) {
        Member member = findMemberById(memberId);
        String password = passwordEncoder.encode(newPassword);
        member.setPassword(password);

        return member;
    }

    @Transactional
    public Member updateLoginId(String newLoginId, Long memberId) {
        Member member = findMemberById(memberId);
        member.setLoginId(newLoginId);

        return member;
    }

    @Transactional
    public Member updateEmail(String email, Long memberId) {
        Member member = findOne(memberId);
        member.setEmail(email);

        return member;
    }

    @Transactional
    public Member updateName(String name, Long memberId) {
        Member member = findOne(memberId);
        member.setName(name);

        return member;
    }

    @Transactional
    public Member updatePhoneNum(String phoneNum, Long memberId) {
        Member member = findOne(memberId);
        member.setPhoneNum(phoneNum);

        return member;
    }

    @Transactional
    public Member updateAddress(AddressUpdateReqDto addressDto, Long memberId) {
        Member member = findOne(memberId);
        Address address = new Address(addressDto.getNewCity(), addressDto.getNewStreet(), addressDto.getNewZipcode(),
                addressDto.getNewDetailedAddress(), member, addressDto.getAddressName());

        return member;
    }

    // 주소 삭제
    @Transactional
    public Member deleteAddress(Long addressId, Long memberId) {
        log.info("주소 삭제 실행={}", addressId);
        Address address = addressRepository.findAddressById(addressId);
        Long findMemberId = address.getMember().getId();
        if (!Objects.equals(memberId, findMemberId)) {
            throw new UnauthorizedAccessException();
        }
        address.setIsActivate(false);
        addressRepository.save(address);

        // 현재 이 로직은 검증이 필요합니다.
        Member member = memberRepository.findMemberById(memberId).orElseThrow(() -> new UserNotExistException());

        return member; // 지금 이건 심각하게 검증이 안된 로직임. 이렇게 해서 더티 체킹 이후에 멤버에 반영이 안될 가능성이 매우 높다.
    }

    private Member findOne(Long memberId) {
        return memberRepository.findMemberById(memberId).orElseThrow(() -> new UserNotExistException());
    }


}
