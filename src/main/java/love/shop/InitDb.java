package love.shop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import love.shop.domain.Member.Member;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDb {

    /*
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


        public void dbInit() {
            Member member = new Member("hello", 28, "이것은 테스트입니다");
            em.persist(member);
        }

    }

     */

}
