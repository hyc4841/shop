package love.shop.domain.member.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import love.shop.domain.member.PasswordAndCheck;

import java.util.Objects;

public class SignupPasswordValidator implements ConstraintValidator<ValidSignupPassword, PasswordAndCheck> {
    @Override
    public boolean isValid(PasswordAndCheck passwordCheck, ConstraintValidatorContext constraintValidatorContext) {
        return Objects.equals(passwordCheck.getPassword(), passwordCheck.getPasswordCheck());
    }
}
