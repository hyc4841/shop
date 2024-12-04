package love.shop.web.login.jwt.repository;

import love.shop.domain.Member.Member;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Member, String> {
}
