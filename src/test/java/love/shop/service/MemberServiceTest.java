package love.shop.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import love.shop.common.exception.UserDuplicationException;
import love.shop.domain.member.Gender;
import love.shop.domain.member.Member;
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
        // given
        SignupRequestDto signupRequest = new SignupRequestDto("Hell5", "1234", "황윤철",
                LocalDate.of(1997, 6, 3), Gender.MAN, "서울", "서울시 은평구 백련산로 6 (응암동, 대주피오레아파트)", "33333", "101동 1103호","dbscjf4841@naver.com");
        // when
        Long signUpMemberId = memberService.signUp(signupRequest);

        // then
        Member member = em.find(Member.class, signUpMemberId);

        assertEquals(signUpMemberId, member.getId());
    }

    @Test(expected = UserDuplicationException.class)
    public void 회원가입_loginId_중복() {
        // given
        SignupRequestDto signupRequest1 = new SignupRequestDto("Hell6", "1234", "황윤철",
                LocalDate.of(1997, 6, 3), Gender.MAN, "서울", "서울시 은평구 백련산로 6 (응암동, 대주피오레아파트)", "33333", "101동 1103호","dbscjf4841@naver.com");
        Long signUpMemberId1 = memberService.signUp(signupRequest1);
        // when
        SignupRequestDto signupRequest2 = new SignupRequestDto("Hell6", "1234", "황윤철",
                LocalDate.of(1997, 6, 3), Gender.MAN, "서울", "서울시 은평구 백련산로 6 (응암동, 대주피오레아파트)", "33333", "101동 1103호","dbscjf4841@naver.com");
        Long signUpMemberId2 = memberService.signUp(signupRequest2);

        fail("회원 중복 예외가 발생해야 한다.");

    }


}