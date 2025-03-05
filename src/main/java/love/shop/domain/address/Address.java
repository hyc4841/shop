package love.shop.domain.address;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import love.shop.domain.delivery.Delivery;
import love.shop.domain.member.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    // 임베디드 타입으로 새로운 값 타입을 정의할 수 있음. 주로 비슷한 정보끼리 묶어서 관리하고 싶을 때 사용한다고 한다.
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "address_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String addressName;

    // 주소 하나에 여러 개의 배송이 생길 수 있다.
    @OneToMany(mappedBy = "address")
    private List<Delivery> delivery = new ArrayList<>();

    @Column
    private String city;
    @Column
    private String street;
    @Column
    private String zipcode;
    @Column
    private String detailedAddress;

    // 리플렉션이나 프록시 같은 기술 때문에 기본 생성자가 필요함. 이때 public으로 완전히 열어두면 좀 그래서 안전하게 protected로 설정
    /*
    protected Address() {
    }
     */

    public Address(String city, String street, String zipcode, String detailedAddress, Member member) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
        this.detailedAddress = detailedAddress;
        this.member = member;
    }
}
