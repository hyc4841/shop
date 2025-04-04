package love.shop.service.member;

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
import love.shop.service.cart.CartService;
import love.shop.service.item.ItemService;
import love.shop.web.login.dto.MemberDto;
import love.shop.web.member.dto.AddressUpdateReqDto;
import love.shop.web.signup.dto.SignupRequestDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    private final ItemService itemService;
    private final CartService cartService;

    // 회원가입
    @Transactional
    public Member signUp(SignupRequestDto signupDto) {
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
        Member signupMember = memberRepository.save(member);

        cartService.createCart(signupMember);

        return member;
    }

    // 중복 확인 예외 처리 확인하기
    private void duplicationValidation(SignupRequestDto signupDto) {
        Member member = memberRepository.findMemberByLoginId(signupDto.getLoginId()).orElseThrow(() -> new RuntimeException());
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
