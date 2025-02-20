package love.shop.web.login.dto;

import lombok.Data;
import love.shop.domain.Address;
import love.shop.domain.member.Gender;
import love.shop.domain.member.Member;
import love.shop.domain.member.MemberRole;
import love.shop.web.address.dto.AddressDto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MemberDto {
    private String loginId;
    private String name;
    private String phoneNum;
    private LocalDate birthDate;
    private Gender gender;
    private List<AddressDto> address;
    private String email;
    private LocalDate joinDate;
    private List<MemberRoleDto> memberRole;

    public MemberDto(Member member) {
        this.loginId = member.getLoginId();
        this.name = member.getName();
        this.phoneNum = member.getPhoneNum();
        this.birthDate = member.getBirthDate();
        this.gender = member.getGender();
        this.address = member.getAddress().stream()
                .map(address -> new AddressDto(address))
                .collect(Collectors.toList());

        this.email = member.getEmail();
        this.joinDate = member.getJoinDate();
        this.memberRole = member.getMemberRole().stream()
                .map(memberRole -> new MemberRoleDto(memberRole))
                .collect(Collectors.toList());
    }
}
