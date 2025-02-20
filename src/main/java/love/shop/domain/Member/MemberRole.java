package love.shop.domain.member;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class MemberRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public MemberRole() {
    }

    public MemberRole(Role role, Member member) {
        this.role = role;
        this.member = member;
    }
}
