package love.shop.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.common.exception.UserDuplicationException;
import love.shop.domain.member.Member;
import love.shop.domain.member.MemberRole;
import love.shop.domain.member.Role;
import love.shop.repository.member.MemberRepository;
import love.shop.repository.member.MemberRoleRepository;
import love.shop.web.login.dto.MemberInfoResDto;
import love.shop.web.signup.dto.SignupRequestDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public Long signUp(SignupRequestDto signupDto) {
        // 중복 검사
        duplicationValidation(signupDto);

        // 회원가입 시작
        // 비밀번호 암호화
        String password = passwordEncoder.encode(signupDto.getPassword());

        // dto 엔티티로 변환
        Member member = signupDto.toMemberEntity(password);

        MemberRole memberRole = new MemberRole(Role.MEMBER, member);
        memberRoleRepository.save(memberRole);

        return memberRepository.save(member);
    }

    private void duplicationValidation(SignupRequestDto signupDto) {
        List<Member> findMember = memberRepository.findMemberByLoginId(signupDto.getLoginId());
        if (!findMember.isEmpty()) {
            throw new UserDuplicationException("이미 등록한 ID가 있습니다");
        }
    }

    public MemberInfoResDto memberInfo(Long memberId) {
        Member member = memberRepository.findMemberById(memberId);
        return new MemberInfoResDto(member.getLoginId(), member.getName(), member.getBirthDate(), member.getGender(),
                member.getAddress(), member.getEmail(), member.getJoinDate(), member.getMemberRole());
    }

    public List<Member> findAllMember() {
        return memberRepository.findAllMember();
    }

    public List<Member> findMemberByName(String memberId) {
        return memberRepository.findMemberByName(memberId);
    }

    // 멤버 pk로 조회
    public Member findMemberById(Long memberId) {
        return memberRepository.findMemberById(memberId);
    }

}
