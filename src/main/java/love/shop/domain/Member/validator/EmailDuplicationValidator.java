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
public class EmailDuplicationValidator implements ConstraintValidator<ValidEmailDuplication, String> {

    private final MemberService memberService;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        // 바꾸려는 이메일이 중복인지 아닌지 확인
        Member member = memberService.findMemberByEmail(email).orElse(null);

        log.info("이메일 중복 여부={}", member);

        return member == null;
    }
}
