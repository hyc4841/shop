package love.shop.web.login.dto;

import lombok.Data;

@Data
public class LogoutResDto {
    private String message;

    public LogoutResDto(String message) {
        this.message = message;
    }
}
