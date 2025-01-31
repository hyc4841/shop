package love.shop.web.signup.dto;

import lombok.Data;
import love.shop.domain.member.Member;

@Data
public class SignupResponseDto {

    private int status;
    private String message;
    private Member member;

    public SignupResponseDto(int status, String message, Member member) {
        this.status = status;
        this.message = message;
        this.member = member;
    }
}
