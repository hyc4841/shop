package love.shop.web.login.dto;

import lombok.Data;
import love.shop.domain.Address;
import love.shop.domain.member.Gender;
import love.shop.domain.member.Member;
import love.shop.domain.member.MemberRole;

import java.time.LocalDate;
import java.util.List;

@Data
public class MemberDto {
    private String loginId;
    private String name;
    private LocalDate birthDate;
    private Gender gender;
    private Address address;
    private String email;
    private LocalDate joinDate;
    private List<MemberRole> memberRole;


    public MemberDto(Member member) {
        this.loginId = member.getLoginId();
        this.name = member.getName();
        this.birthDate = member.getBirthDate();
        this.gender = member.getGender();
        this.address = member.getAddress();
        this.email = member.getEmail();
        this.joinDate = member.getJoinDate();
        this.memberRole = member.getMemberRole();
    }
}
