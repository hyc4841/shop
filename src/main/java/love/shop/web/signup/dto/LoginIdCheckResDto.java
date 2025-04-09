package love.shop.web.signup.dto;

import lombok.Data;

@Data
public class LoginIdCheckResDto {
    private String loginId;
    private String message;
    private Integer status;

    public LoginIdCheckResDto(String loginId, String message, Integer status) {
        this.loginId = loginId;
        this.message = message;
        this.status = status;
    }
}
