package love.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.Member.Member;
import love.shop.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final MemberRepository memberRepository;

    public List<Member> findAllUser() {
        return memberRepository.findAllUser();
    }

    public Member findUser(String userId) {
        return memberRepository.findUser(userId);
    }

}
