package love.shop.web.login.dto;

import lombok.Data;
import love.shop.domain.Member.Gender;
import love.shop.domain.Member.Member;
import love.shop.domain.Member.MemberRole;

@Data
public class SignupRequestDto {

    private String name;
    private Integer age;
    private String memo;
    private String password;
    private Gender gender;

    public Member toEntity(String name, Integer age, String memo, String password, Gender gender) {
        return new Member(name, age, memo, password, gender);
    }
}
