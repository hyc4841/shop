package love.shop.domain.member.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailBlankValidator implements ConstraintValidator<ValidEmailBlank, String> {

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {

        boolean matches = email.matches(".*\\s+.*");// 정규 표현식으로 공백 포함되어 있는지 검사
        log.info("공백검사={}", matches);

        return !matches;
    }
}
