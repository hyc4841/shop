package love.shop.web.signup.dto;

import lombok.Data;

@Data
public class EmailCertificationConfirmResDto {

    private String email;
    private String message;
    private Integer status;

    public EmailCertificationConfirmResDto(String email, String message, Integer status) {
        this.email = email;
        this.message = message;
        this.status = status;
    }
}
