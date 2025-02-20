package love.shop.repository.Address;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.Address;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AddressRepository {

    private final EntityManager em;

    public void save(Address address) {
        em.persist(address);
    }
}
