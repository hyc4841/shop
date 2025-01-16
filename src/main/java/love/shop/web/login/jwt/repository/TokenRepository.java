package love.shop.web.login.jwt.repository;

import love.shop.domain.member.Member;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Member, String> {
}
