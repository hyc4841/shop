package love.shop.web.login.dto;

import lombok.Data;
import love.shop.domain.Address;
import love.shop.domain.member.Gender;
import love.shop.domain.member.MemberRole;

import java.time.LocalDate;
import java.util.List;

@Data
public class MemberInfoResDto {
    private String loginId;
    private String name;
    private LocalDate birthDate;
    private Gender gender;
    private Address address;
    private String email;
    private LocalDate joinDate;
    private List<MemberRole> memberRole;


    public MemberInfoResDto(String loginId, String name, LocalDate birthDate, Gender gender, Address address, String email, LocalDate joinDate, List<MemberRole> memberRole) {
        this.loginId = loginId;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.email = email;
        this.joinDate = joinDate;
        this.memberRole = memberRole;
    }
}
