package love.shop.domain.member.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import love.shop.domain.member.PasswordAndCheck;

public class SignupPasswordBlankValidator implements ConstraintValidator<ValidPasswordNotBlank, PasswordAndCheck> {
    @Override
    public boolean isValid(PasswordAndCheck passwordAndCheck, ConstraintValidatorContext constraintValidatorContext) {
        return !passwordAndCheck.getPassword().isBlank();
    }
}
