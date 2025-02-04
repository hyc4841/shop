package love.shop.domain.member;

import jakarta.persistence.*;
import lombok.Getter;
import love.shop.domain.Address;
import love.shop.domain.order.Order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "login_id", nullable = false, length = 50)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column
    private LocalDate birthDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Embedded // 값 타입을 사용하는 곳에 사용
    private Address address;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate joinDate;

    // 멤버 한명이 가질 수 있는 권한이 여러개일 수 있다면 이것은 말이 됨.
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberRole> memberRole = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    public Member() {
    }

    public Member(String loginId, String password, String name, LocalDate birthDate, Gender gender, Address address, String email, LocalDate joinDate) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.email = email;
        this.joinDate = joinDate;
    }


}