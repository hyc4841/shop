package love.shop.repository.member;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.member.MemberRole;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRoleRepository {

    private final EntityManager em;

    public MemberRole findMemberRoleById(String memberId) {
        return em.find(MemberRole.class, memberId);
    }

    public void save(MemberRole memberRole) {
        em.persist(memberRole);
    }
}
