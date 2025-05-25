package love.shop.domain.member.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginIdBlankValidator implements ConstraintValidator<ValidLoginIdBlank, String> {

    @Override
    public boolean isValid(String loginId, ConstraintValidatorContext constraintValidatorContext) {

        boolean contains = loginId.contains(" "); // 공백 포함되어 있는지 검사

        return !contains;
    }
}
