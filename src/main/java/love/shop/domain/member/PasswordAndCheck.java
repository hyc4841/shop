package love.shop.domain.member;

import lombok.Data;

@Data
public class PasswordAndCheck {
    private String password;
    private String passwordCheck;

    public PasswordAndCheck(String password, String passwordCheck) {
        this.password = password;
        this.passwordCheck = passwordCheck;
    }
}
