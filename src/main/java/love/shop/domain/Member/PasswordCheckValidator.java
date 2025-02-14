package love.shop.domain.member;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.service.member.MemberService;
import love.shop.web.login.dto.CustomUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordCheckValidator implements ConstraintValidator<ValidPasswordCheck, String> {

    private final MemberService memberService;

    @Override
    public boolean isValid(String curPwd, ConstraintValidatorContext context) {
        // 현재 비밀번호 확인
        log.info("curPwd={}", curPwd);
        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId();
        return memberService.curPasswordCheck(curPwd, memberId);
    }
}
