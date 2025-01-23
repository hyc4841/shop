package love.shop.web.signup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import love.shop.domain.Address;
import love.shop.domain.member.Gender;
import love.shop.domain.member.Member;
import love.shop.domain.member.ValidGender;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class SignupRequestDto {

    @NotBlank(message = "아이디는 꼭 필요합니다")
    private String loginId;
    // 만약 검증에 개입하는 어노테이션이 두 개 이상이면 메시지 우선순위 어떻게 되는거지?. 지금은 시스템에 먼저 입력되는 애가 걸림.
    // 두 어노테이션에 해당하는 필드 오류가 모두 잡히고. 마지막으로 잡힌 애가 응답 데이터에 실리게 됨.
    @NotBlank(message = "비밀번호는 최소 8자 이상으로 입력해주세요.")
    @Length(min = 8, message = "비밀번호는 최소 8자 이상으로 입력해주세요")
    private String password;
    @NotBlank(message = "이름은 꼭 필요합니다")
    private String name;
    @NotNull(message = "생년월일을 입력해주세요") // LocalDate 라는 객체 타입이기 때문에 NotNull을 사용해야 한다.
    private LocalDate birthDate;
    @ValidGender
    private Gender gender;
    @NotBlank
    private String city;
    @NotBlank(message = "주소는 꼭 필요합니다")
    private String street;
    @NotBlank(message = "주소는 꼭 필요합니다")
    private String zipcode;
    @NotBlank(message = "상세주소는 꼭 필요합니다")
    private String detailedAddress;
    @NotBlank(message = "이메일은 꼭 필요합니다")
    private String email;

    public SignupRequestDto(String loginId, String password, String name, LocalDate birthDate, Gender gender, String city, String street, String zipcode, String detailedAddress, String email) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
        this.detailedAddress = detailedAddress;
        this.email = email;
    }

    public Member toMemberEntity() {
        Address address = new Address(this.getCity(), this.getStreet(), this.getZipcode(),
                this.getDetailedAddress());
        return new Member(this.getLoginId(), this.getPassword(), this.getName(),
                this.getBirthDate(), this.getGender(), address, this.getEmail(), LocalDate.now());
    }

}
