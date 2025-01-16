package love.shop.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.member.Member;
import love.shop.repository.MemberRepository;
import love.shop.web.login.dto.CustomUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 로그인시 실행되는 부분
    // 이 부분이 실제로 데이터베이스에 접근해서 해당 userId를 갖고 있는
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
            log.info("유저 찾기 실행");
            Member member = memberRepository.findMemberByLoginId(loginId);
        if (member != null) {
            return createUserDetails(member);
        } else {
            log.info("유저를 찾을 수 없음");
            throw new UsernameNotFoundException("유저를 찾을 수 없습니다.");
        }
    }

    private UserDetails createUserDetails(Member member) {
        List<GrantedAuthority> grantedAuthorities = member.getMemberRole().stream()
                .map(authority -> new SimpleGrantedAuthority("ROLE_" + authority.getRole()))
                .collect(Collectors.toList());


        return new CustomUser(
                member.getId(),
                member.getName(),
                member.getPassword(),
                grantedAuthorities
        );
    }
}
