package love.shop.web.login.dto;

import lombok.Data;
import love.shop.domain.member.MemberRole;
import love.shop.domain.member.Role;

@Data
public class MemberRoleDto {

    private Role role;
//    private Long memberId;

    public MemberRoleDto(MemberRole memberRole) {
        this.role = memberRole.getRole();
//        this.memberId = memberRole.getMember().getId();
    }
}
