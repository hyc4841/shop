package love.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.common.exception.UserDuplicationException;
import love.shop.domain.member.Member;
import love.shop.domain.member.MemberRole;
import love.shop.domain.member.Role;
import love.shop.repository.MemberRepository;
import love.shop.repository.MemberRoleRepository;
import love.shop.web.login.dto.MemberInfoResDto;
import love.shop.web.signup.dto.SignupRequestDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public Long signUp(SignupRequestDto signupDto) {

        // 중복 검사
        Member member = memberRepository.findUser(signupDto.getLoginId());
        if (member != null) {
            throw new UserDuplicationException("이미 등록한 ID가 있습니다");
        }

        String password = passwordEncoder.encode(signupDto.getPassword());

        member = signupDto.toMemberEntity(signupDto.getLoginId(), password, signupDto.getName(), signupDto.getBirthDate(),
                signupDto.getGender(), signupDto.getCity(), signupDto.getStreet(), signupDto.getZipcode(),
                signupDto.getDetailedAddress(),signupDto.getEmail());

        MemberRole memberRole = new MemberRole(Role.MEMBER, member);
        memberRoleRepository.save(memberRole);

        return memberRepository.save(member);
    }

    public MemberInfoResDto memberInfo(Long memberId) {
        Member member = memberRepository.findUserById(memberId);
        return new MemberInfoResDto(member.getLoginId(), member.getName(), member.getBirthDate(), member.getGender(),
                member.getAddress(), member.getEmail(), member.getJoinDate(), member.getMemberRole());
    }

    public List<Member> findAllUser() {
        return memberRepository.findAllUser();
    }

    public Member findUser(String userId) {
        return memberRepository.findUser(userId);
    }

    public Member findUserById(Long memberId) {
        return memberRepository.findUserById(memberId);
    }

}
