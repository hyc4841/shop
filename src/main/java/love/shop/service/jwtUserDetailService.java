package love.shop.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.Member.Member;
import love.shop.repository.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class jwtUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    // 로그인시 실행되는 부분
    // 이 부분이 실제로 데이터베이스에 접근해서 해당 userId를 갖고 있는
    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {

        try {
            Member member = memberRepository.findUser(memberId);
            return createUserDetails(member);
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("해당 유저가 없습니다.");
        }
    }

    private UserDetails createUserDetails(Member member) {
        User user = new User(
                member.getName(),
                passwordEncoder.encode(member.getPassword()),
                member.getMemberRole().stream()
                        .map(memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.getRole()))
                        .collect(Collectors.toList())
        );

        log.info("userDetails={}", user.getAuthorities());
        return user;
    }

}
