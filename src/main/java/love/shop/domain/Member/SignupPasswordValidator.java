package love.shop.domain.member;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class SignupPasswordValidator implements ConstraintValidator<ValidSignupPassword, PasswordAndCheck> {
    @Override
    public boolean isValid(PasswordAndCheck passwordCheck, ConstraintValidatorContext constraintValidatorContext) {
        return Objects.equals(passwordCheck.getPassword(), passwordCheck.getPasswordCheck());
    }
}
