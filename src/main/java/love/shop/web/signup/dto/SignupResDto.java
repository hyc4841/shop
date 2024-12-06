package love.shop.web.signup.dto;

import lombok.Data;
import love.shop.domain.Member.Member;

@Data
public class SignupResDto {

    private int status;
    private String message;
    private Member member;

    public SignupResDto(int status, String message, Member member) {
        this.status = status;
        this.message = message;
        this.member = member;
    }
}
