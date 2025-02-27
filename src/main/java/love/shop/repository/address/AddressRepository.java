package love.shop.repository.address;

import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.address.Address;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AddressRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    // 주소 저장
    public void save(Address address) {
        em.persist(address);
    }

    // 주소 조회
    public Address findAddressById(Long id) {
        return em.find(Address.class, id);
    }
}
