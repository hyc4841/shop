package love.shop.domain.Member;

import jakarta.persistence.*;
import lombok.Getter;
import love.shop.web.login.dto.MemberInfoResDto;

import java.util.List;

@Getter
@Entity
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String memo;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberRole> memberRole;

    public Member() {
    }

    public Member(String name, Integer age, String memo, String password, Gender gender) {
        this.name = name;
        this.age = age;
        this.memo = memo;
        this.password = password;
        this.gender = gender;
    }

    public MemberInfoResDto toDto() {
        return new MemberInfoResDto(name, age, memo, gender);
    }
}