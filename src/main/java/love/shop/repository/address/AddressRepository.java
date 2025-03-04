package love.shop.repository.address;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.address.Address;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AddressRepository {

    private final EntityManager em;

    // 주소 저장
    public void save(Address address) {
        em.persist(address);
    }

    // 주소 조회
    public Address findAddressById(Long id) {
        return em.find(Address.class, id);
    }
}
