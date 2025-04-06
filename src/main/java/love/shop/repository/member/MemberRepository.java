package love.shop.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.member.Member;
import love.shop.domain.member.QMember;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    QMember member = QMember.member;

    public void save(Member member) {
        em.persist(member);
    }

    public List<Member> findAllMember() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public Optional<Member> findMemberById(Long memberId) {
        return Optional.ofNullable(queryFactory.selectFrom(member)
                .leftJoin(member.cart).fetchJoin()
                .where(member.id.eq(memberId))
                .fetchOne());
    }

    // 이름으로 멤버 찾기
    public List<Member> findMemberByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

    // 굳이 이렇게 try catch 문으로 안해도 될거같음 고쳐보자
    public Optional<Member> findMemberByLoginId(String loginId) {
        return Optional.ofNullable(queryFactory.selectFrom(member)
                .leftJoin(member.cart).fetchJoin()
                .where(member.loginId.eq(loginId))
                .fetchOne());
    }

    public Optional<Member> findMemberByEmail(String email) {
        return Optional.ofNullable(queryFactory.selectFrom(member)
                .leftJoin(member.cart).fetchJoin()
                .where(member.email.eq(email))
                .fetchOne());
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
