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

    public List<Member> findAllMember() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public Member findMemberById(Long memberId) {
        return em.find(Member.class, memberId);
    }

    // 이름으로 멤버 찾기
    public List<Member> findMemberByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

    // 굳이 이렇게 try catch 문으로 안해도 될거같음 고쳐보자
    public List<Member> findMemberByLoginId(String loginId) {
        return em.createQuery("select m from Member m where m.loginId = :login_id", Member.class)
                .setParameter("login_id", loginId)
                .getResultList();
    }

    public Long save(Member member) {
        em.persist(member);
        Member saveMember = em.find(Member.class, member.getId());
        return saveMember.getId();
    }

}
