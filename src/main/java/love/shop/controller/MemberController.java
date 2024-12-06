package love.shop.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.Member.Member;
import love.shop.service.LoginService;
import love.shop.service.MemberService;
import love.shop.web.login.dto.CustomUser;
import love.shop.web.login.dto.LoginDto;
import love.shop.web.login.dto.MemberInfoResDto;
import love.shop.web.signup.dto.SignupResDto;
import love.shop.web.login.jwt.JwtToken;
import love.shop.web.signup.dto.SignupRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final LoginService loginService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResDto> signup(@RequestBody SignupRequestDto signupDto) {
        log.info("회원가입 시작={}", signupDto);
        Long memberId = memberService.signUp(signupDto);

        // 회원가입 성공한 멤버 데이터베이스에서 다시 꺼내서 확인
        Member member = memberService.findUserById(memberId);

        return ResponseEntity.ok(new SignupResDto(200, "회원가입 성공", member));
    }

    // 로그인 검사에 통과하면 토큰을 발급해준다.
    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        log.info("로그인 시도");
        log.info("loginDto={}", loginDto);
        JwtToken tokenInfo = loginService.login(loginDto.getLoginId(), loginDto.getPassword(), response);

        return ResponseEntity.ok(new JwtToken(tokenInfo.getGrantType(), tokenInfo.getAccessToken(), null));
    }

    // 멤버 정보 조회 정보 조회
    @GetMapping("/member/info")
    public ResponseEntity<MemberInfoResDto> memberInfo() {
        // 토큰 안에 있는 memberId로 멤버 조회. 엑세스 토큰에 아예 memberId가 박혀 있다
        // 토큰 정보는 SecurityContextHolder에 있다. 이것 안에 memberId를 가져온다.
        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId();

        log.info("memberInfo 실행");
        MemberInfoResDto memberInfo = memberService.memberInfo(memberId);
        return ResponseEntity.ok(memberInfo);
    }
}
