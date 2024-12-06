package love.shop.web.login.dto;

import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUser extends User {

    private Long memberId;
    private String name;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUser(Long memberId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
