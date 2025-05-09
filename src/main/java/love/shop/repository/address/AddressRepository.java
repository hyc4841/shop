package love.shop.repository.address;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.address.Address;
import love.shop.domain.address.QAddress;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor

public class AddressRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    QAddress address = QAddress.address;

    // 주소 저장
    public void save(Address address) {
        em.persist(address);
    }

    // 주소 조회
    public Address findAddressById(Long id) {
        return em.find(Address.class, id);
    }

    // 멤버 id로 주소 조회
    public List<Address> findAddressesByMemberId(Long memberId) {
        return queryFactory.selectFrom(address)
                .where(address.member.id.eq(memberId))
                .fetch();
    }
}
