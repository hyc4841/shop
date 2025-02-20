package love.shop.repository.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import love.shop.domain.member.Member;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class MemberRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 조건검색테스트() {
        // given
        String name = "황윤철";
        // when
        List<Member> members =  memberRepository.findMembersBySearch(name);
        // then
        Assertions.assertNotNull(members);
    }

}