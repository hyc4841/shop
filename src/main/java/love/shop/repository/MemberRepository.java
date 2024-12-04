package love.shop.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.Member.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public List<Member> findAllUser() {
        return em.createQuery("select u from User u", Member.class)
                .getResultList();
    }

    public Member findUser(String userId) {
        return em.createQuery("select u from User u where u:userId = :userId", Member.class)
                .setParameter("userId", userId)
                .getSingleResult();
    }

}
