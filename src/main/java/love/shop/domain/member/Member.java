package love.shop.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.address.Address;
import love.shop.domain.order.Order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "login_id", nullable = false, length = 50, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private String phoneNum;

    @Column
    private LocalDate birthDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Address> address = new ArrayList<>();

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate joinDate;

    // 멤버 한명이 가질 수 있는 권한이 여러개일 수 있다면 이것은 말이 됨.
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberRole> memberRole = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    public Member(String loginId, String password, String name, String phoneNum, LocalDate birthDate, Gender gender, String email, LocalDate joinDate) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.phoneNum = phoneNum;
        this.birthDate = birthDate;
        this.gender = gender;
        this.email = email;
        this.joinDate = joinDate;
    }

    // 비밀번호 set 열어놔도 괜찮을까?
    public void setPassword(String password) {
        this.password = password;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }




}