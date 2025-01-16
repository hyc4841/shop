package love.shop.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import love.shop.domain.member.Gender;
import love.shop.domain.member.Member;
import love.shop.repository.MemberRepository;
import love.shop.web.signup.dto.SignupRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void 회원가입() {
        // given
        SignupRequestDto signupRequest = new SignupRequestDto("Hell4", "1234", "황윤철",
                LocalDate.of(1997, 6, 3), Gender.MAN, "서울", "서울시 은평구 백련산로 6 (응암동, 대주피오레아파트)", "33333", "101동 1103호","dbscjf4841@naver.com");
        // when
        Long signUpMemberId = memberService.signUp(signupRequest);

        // then
        Member member = em.find(Member.class, signUpMemberId);

        assertEquals(signUpMemberId, member.getId());
    }
}