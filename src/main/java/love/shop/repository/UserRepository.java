package love.shop.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public List<User> findAllUser() {
        return em.createQuery("select u from User u", User.class)
                .getResultList();
    }

    public List<User> findUser(String userId) {
        return em.createQuery("select u from User u where u:userId = :userId", User.class)
                .setParameter("userId", userId)
                .getResultList();
    }

}
