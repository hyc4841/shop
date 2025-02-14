package love.shop.domain.member;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.service.member.MemberService;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginIdValidator implements ConstraintValidator<ValidLoginId, String> {

    private final MemberService memberService;

    @Override
    public boolean isValid(String loginId, ConstraintValidatorContext constraintValidatorContext) {
        // 1. 아이디 중복 검증
        List<Member> members = memberService.findMemberByLoginId(loginId);

        if (!members.isEmpty()) {
            // members가 비어 있어야 해당 아이디를 쓰고 있는 유저가 없다는 뜻.
            return false;
        }
        return true;
    }
}
