package love.shop.web.login.dto;

import lombok.Data;
import love.shop.domain.Member.Gender;

@Data
public class MemberInfoResDto {
    private String name;
    private Integer age;
    private String memo;
    private Gender gender;

    public MemberInfoResDto(String name, Integer age, String memo, Gender gender) {
        this.name = name;
        this.age = age;
        this.memo = memo;
        this.gender = gender;
    }
}
