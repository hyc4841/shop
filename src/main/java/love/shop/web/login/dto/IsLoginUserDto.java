package love.shop.web.login.dto;

import lombok.Data;

@Data
public class IsLoginUserDto {
    private String userName;

    public IsLoginUserDto(String userName) {
        this.userName = userName;
    }
}
