package love.shop.web.signup.dto;

import lombok.Data;
import love.shop.domain.Address;
import love.shop.domain.member.Gender;
import love.shop.domain.member.Member;

import java.time.LocalDate;

@Data
public class SignupRequestDto {

    private String loginId;
    private String password;
    private String name;
    private LocalDate birthDate;
    private Gender gender;
    private String city;
    private String street;
    private String zipcode;
    private String detailedAddress;
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
