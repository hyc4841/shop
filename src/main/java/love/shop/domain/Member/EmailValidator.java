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
public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private final MemberService memberService;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        // 바꾸려는 이메일이 중복인지 아닌지 확인
        List<Member> memberByEmail = memberService.findMemberByEmail(email);

        if (memberByEmail.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
