package love.shop.web.signup.dto;

import lombok.Data;
import love.shop.domain.Member.Gender;
import love.shop.domain.Member.Member;

@Data
public class SignupRequestDto {

    private String name;
    private Integer age;
    private String memo;
    private String password;
    private Gender gender;

    public SignupRequestDto(String name, Integer age, String memo, String password, Gender gender) {
        this.name = name;
        this.age = age;
        this.memo = memo;
        this.password = password;
        this.gender = gender;
    }

    public Member toEntity(String name, Integer age, String memo, String password, Gender gender) {
        return new Member(name, age, memo, password, gender);
    }
}
