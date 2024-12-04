package love.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.Member.Member;
import love.shop.domain.Member.MemberRole;
import love.shop.domain.Member.Role;
import love.shop.repository.MemberRepository;
import love.shop.repository.MemberRoleRepository;
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
        Member member = memberRepository.findUser(signupDto.getName());
        if (member != null) {
            throw new IllegalStateException("이미 등록된 ID 입니다.");
        }

        String password = passwordEncoder.encode(signupDto.getPassword());

        member = signupDto.toEntity(signupDto.getName(), signupDto.getAge(), signupDto.getMemo(),
                password, signupDto.getGender());

        Long signupMemberId = memberRepository.save(member);

        MemberRole memberRole = new MemberRole(Role.MEMBER, member);
        memberRoleRepository.save(memberRole);

        return signupMemberId;
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
