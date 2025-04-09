package love.shop.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import love.shop.common.exception.UserDuplicationException;
import love.shop.domain.member.Gender;
import love.shop.domain.member.Member;
import love.shop.domain.member.PasswordAndCheck;
import love.shop.service.member.MemberService;
import love.shop.web.signup.dto.SignupRequestDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void 회원가입() {
        /*
        // given
        PasswordAndCheck passwordAndCheck = new PasswordAndCheck("1234", "1234");
        SignupRequestDto signupRequest = new SignupRequestDto("Hell5", passwordAndCheck, "황윤철", "01099694841",
                "dbscjf4841@naver.com", LocalDate.of(1997, 6, 3), Gender.MAN, "서울", "서울시 은평구 백련산로 6 (응암동, 대주피오레아파트)", "33333", "101동 1103호");
        // when
        Member signUpMember = memberService.signUp(signupRequest);

        // then
        Member member = em.find(Member.class, signUpMember.getId());

        assertEquals(signUpMember.getId(), member.getId());

         */
    }

    @Test(expected = UserDuplicationException.class)
    public void 회원가입_loginId_중복() {
        // given
        PasswordAndCheck passwordAndCheck = new PasswordAndCheck("1234", "1234");
        SignupRequestDto signupRequest1 = new SignupRequestDto("Hell6", passwordAndCheck, "황윤철", "01099694841",
                "dbscjf4841@naver.com", LocalDate.of(1997, 6, 3), Gender.MAN, "서울", "서울시 은평구 백련산로 6 (응암동, 대주피오레아파트)", "33333", "101동 1103호");
        SignupRequestDto signupRequest2 = new SignupRequestDto("Hell6", passwordAndCheck, "황윤철", "01099694841",
                "dbscjf4841@naver.com", LocalDate.of(1997, 6, 3), Gender.MAN, "서울", "서울시 은평구 백련산로 6 (응암동, 대주피오레아파트)", "33333", "101동 1103호");
        // when
        /*
        Member member1 = memberService.signUp(signupRequest2);
        Member member2 = memberService.signUp(signupRequest1);

         */

        // then
        fail("회원 중복 예외가 발생해야 한다.");
    }


}