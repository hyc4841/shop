package love.shop.web.login.jwt.repository;

import love.shop.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<User, String> {
}
