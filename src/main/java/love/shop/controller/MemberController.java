package love.shop.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.Member.Member;
import love.shop.service.LoginService;
import love.shop.service.MemberService;
import love.shop.web.login.dto.LoginDto;
import love.shop.web.login.dto.SignupResDto;
import love.shop.web.login.jwt.JwtToken;
import love.shop.web.signup.dto.SignupRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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


    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        log.info("로그인 시도");
        log.info("loginDto={}", loginDto);
        JwtToken tokenInfo = loginService.login(loginDto.getUserId(), loginDto.getPassword(), response);

        return ResponseEntity.ok(new JwtToken(tokenInfo.getAccessToken(), null));
    }



}
