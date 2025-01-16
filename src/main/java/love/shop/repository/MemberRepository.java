package love.shop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.member.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public List<Member> findAllUser() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    // 이름으로 멤버 찾기
    public Member findUser(String name) {
        try {
            return em.createQuery("select m from Member m where m.name = :name", Member.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            log.info("결과 없음");
            return null;
        }
    }

    public Member findMemberByLoginId(String loginId) {
        try {
            return em.createQuery("select m from Member m where m.loginId = :login_id", Member.class)
                    .setParameter("login_id", loginId)
                    .getSingleResult();
        } catch (NoResultException e) {
            log.info("해당 유저 찾을 수 없음");
            return null;
        }
    }

    public Member findUserById(Long memberId) {
        return em.find(Member.class, memberId);
    }

    public Long save(Member member) {
        em.persist(member);
        Member saveMember = em.find(Member.class, member.getId());
        return saveMember.getId();
    }

}
