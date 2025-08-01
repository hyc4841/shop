package love.shop.domain.member.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.member.Gender;

@Slf4j
public class GenderValidator implements ConstraintValidator<ValidGender, Gender> {
    @Override
    public boolean isValid(Gender gender, ConstraintValidatorContext context) {
        log.info("성별에 NONE 값이 들어오면 안됨");
        log.info("gender 값={}", gender);
        return gender != null && !gender.equals(Gender.NONE);
    }

}
