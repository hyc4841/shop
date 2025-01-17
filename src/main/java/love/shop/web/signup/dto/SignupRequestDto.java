package love.shop.web.signup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import love.shop.domain.Address;
import love.shop.domain.member.Gender;
import love.shop.domain.member.Member;
import love.shop.domain.member.ValidGender;

import java.time.LocalDate;

@Data
public class SignupRequestDto {

    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;
    @NotEmpty
    private String name;

    private LocalDate birthDate;

    @ValidGender
    private Gender gender;

    private String city;
    @NotEmpty
    private String street;
    @NotEmpty
    private String zipcode;
    @NotEmpty
    private String detailedAddress;
    @NotEmpty
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

    public Member toMemberEntity(String loginId, String password, String name, LocalDate birthDate, Gender gender, String city, String street, String zipcode, String detailedAddress, String email) {
        Address address = new Address(city, street, zipcode, detailedAddress);
        return new Member(loginId, password, name, birthDate, gender, address, email, LocalDate.now());
    }


}
