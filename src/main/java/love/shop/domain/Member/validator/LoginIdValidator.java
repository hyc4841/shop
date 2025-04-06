package love.shop.domain.member.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.member.Member;
import love.shop.service.member.MemberService;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class LoginIdValidator implements ConstraintValidator<ValidLoginId, String> {

    private final MemberService memberService;

    @Override
    public boolean isValid(String loginId, ConstraintValidatorContext constraintValidatorContext) {
        // 1. 아이디 중복 검증
        Member member = memberService.findMemberByLoginId(loginId).orElse(null);

        log.info("아이디 중복 검사={}", member);

        return member == null;
    }
}
