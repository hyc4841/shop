package love.shop.service.member;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.common.exception.UserDuplicationException;
import love.shop.common.exception.UserNotExistException;
import love.shop.domain.address.Address;
import love.shop.domain.member.Member;
import love.shop.domain.member.MemberRole;
import love.shop.domain.member.Role;
import love.shop.repository.address.AddressRepository;
import love.shop.repository.member.MemberRepository;
import love.shop.repository.member.MemberRoleRepository;
import love.shop.service.RedisService;
import love.shop.service.cart.CartService;
import love.shop.web.login.dto.MemberDto;
import love.shop.web.member.dto.AddressUpdateReqDto;
import love.shop.web.signup.dto.SignupRequestDto;
import org.springframework.http.HttpStatus;
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
    private final MemberRoleRepository memberRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;
    private final CartService cartService;

    private final JavaMailSender javaMailSender;
    private final RedisService redisService;


    // 회원가입
    @Transactional
    public Member signUp(SignupRequestDto signupDto, BindingResult bindingResult) throws MethodArgumentNotValidException {

        // 여기서도 최종적으로 redis에서 이메일 인증 확인, 에메일 일치 여부를 확인해야함

        String email = signupDto.getEmail();
        String certificationStatus = redisService.getValue(email);
        if (!Objects.equals(certificationStatus, "ok")) {
            FieldError error = new FieldError("emailCertificationConfirmDto", "email",
                    "해당 이메일로 인증 코드가 전송된 적이 없습니다. 먼저 인증 요청을 해주세요");
            bindingResult.addError(error);
            throw new MethodArgumentNotValidException(null, bindingResult);
        }

        // 중복 검사
        duplicationValidation(signupDto);

        // 회원가입 시작
        // 비밀번호 암호화
        String password = passwordEncoder.encode(signupDto.getPasswordAndCheck().getPassword());

        // dto 엔티티로 변환
        Member member = signupDto.toMemberEntity(password);

        Address address = new Address(signupDto.getCity(), signupDto.getStreet(), signupDto.getZipcode(), signupDto.getDetailedAddress(), member);
        addressRepository.save(address);

        MemberRole memberRole = new MemberRole(Role.MEMBER, member);
        memberRoleRepository.save(memberRole);
        memberRepository.save(member);

        cartService.createCart(member);

        redisService.removeData(email);

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
        message.setText(code);

        javaMailSender.send(message);
    }

    // 이메일 인증 코드 확인
    @Transactional
    public void confirmEmailCertification(String email, String code, BindingResult bindingResult) throws MethodArgumentNotValidException {

        String matchingEmail = redisService.getValue(code);
        String isSent = redisService.getValue(email);

        log.info("입력한 코드={}", code);
        log.info("이메일 맞나?={}", matchingEmail);
        log.info("해당 이메일로 인증 코드를 보낸적이 있나?={}", isSent);

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
            log.info("데이터 어떻게 되는 지 확인={}", redisService.getValue(email));

        } else {
            // 아니면 오류를 뱉는다.
            FieldError error = new FieldError("emailCertificationConfirmDto", "code", "잘못된 인증 코드입니다.");
            bindingResult.addError(error);
//            bindingResult.rejectValue("code", String.valueOf(HttpStatus.BAD_REQUEST), "잘못된 인증 코드입니다");

            throw new MethodArgumentNotValidException(null, bindingResult);
        }


    }


    // 중복 확인 예외 처리 확인하기
    private void duplicationValidation(SignupRequestDto signupDto) {
        Member member = memberRepository.findMemberByLoginId(signupDto.getLoginId()).orElse(null);
        if (member != null) {
            throw new UserDuplicationException("이미 등록한 ID가 있습니다");
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
        Address address = new Address(addressDto.getNewCity(), addressDto.getNewStreet(), addressDto.getNewZipcode(), addressDto.getNewDetailedAddress(), member);

        return member;
    }

    private Member findOne(Long memberId) {
        return memberRepository.findMemberById(memberId).orElseThrow(() -> new UserNotExistException());
    }


}
