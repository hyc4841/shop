package love.shop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable // 값 타입을 정의하는 곳에 사용
@Getter
public class Address {
    // Address는 임베디드 타입. 기본 값 타입을 모아서 만들기 때문에 복합 값 타입이라고도 한다.
    // 임베디드 타입으로 새로운 값 타입을 정의할 수 있음. 주로 비슷한 정보끼리 묶어서 관리하고 싶을 때 사용한다고 한다.
    private String city;
    private String street;
    private String zipcode;
    private String detailedAddress;

    // 리플렉션이나 프록시 같은 기술 때문에 기본 생성자가 필요함. 이때 public으로 완전히 열어두면 좀 그래서 안전하게 protected로 설정
    protected Address() {
    }

    public Address(String city, String street, String zipcode, String detailedAddress) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
        this.detailedAddress = detailedAddress;
    }
}
