package love.shop.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.address.Address;
import love.shop.domain.cart.Cart;
import love.shop.domain.order.Order;
import love.shop.domain.review.Review;

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

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDate joinDate;

    @Column
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Address> address = new ArrayList<>(); // 회원 주소

    // 멤버 한명이 가질 수 있는 권한이 여러개일 수 있다면 이것은 말이 됨.
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberRole> memberRole = new ArrayList<>(); // 회원 등급

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();  // 주문

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>(); // 리뷰

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Cart cart;  // 장바구니

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

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void addAddress(Address address) {
        this.address.add(address);
    }

    public void addMemberRole(MemberRole memberRole) {
        this.memberRole.add(memberRole);
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }




}