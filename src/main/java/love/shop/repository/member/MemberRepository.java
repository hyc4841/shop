package love.shop.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.member.Member;
import love.shop.domain.member.QMember;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    QMember member = QMember.member;



    public Long save(Member member) {
        em.persist(member);
        Member saveMember = em.find(Member.class, member.getId());
        return saveMember.getId();
    }

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

    public List<Member> findMemberByEmail(String email) {
        return em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();
    }

    public List<Member> findMembersBySearch(String keyword) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<Member> members = queryFactory.selectFrom(member)
                .where(member.name.eq(keyword))
                .fetch();

        return members;
    }
/*
    public Address findAddressById(Long addressId) {
        queryFactory.select(member)
                .from(member)
                .join(member.address)
                .where(member.address.id)
    }

 */





}
