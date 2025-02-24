package love.shop.domain.member;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SignupPasswordBlankValidator implements ConstraintValidator<ValidPasswordNotBlank, PasswordAndCheck> {
    @Override
    public boolean isValid(PasswordAndCheck passwordAndCheck, ConstraintValidatorContext constraintValidatorContext) {
        return !passwordAndCheck.getPassword().isBlank();
    }
}
