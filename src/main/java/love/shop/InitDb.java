package love.shop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import love.shop.domain.Member.Gender;
import love.shop.domain.Member.Member;
import love.shop.service.MemberService;
import love.shop.web.signup.dto.SignupRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService service;


    @PostConstruct
    public void init() {
        service.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        private final MemberService memberService;


        public void dbInit() {
            SignupRequestDto member = new SignupRequestDto("Hell4", 20, "이것은 테스트 입니다.", "1234", Gender.MAN);
            memberService.signUp(member);
        }

    }
}
