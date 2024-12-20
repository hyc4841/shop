package love.shop.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.Member.Member;
import love.shop.service.LoginService;
import love.shop.service.MemberService;
import love.shop.service.RedisService;
import love.shop.web.login.dto.*;
import love.shop.web.login.jwt.JwtTokenProvider;
import love.shop.web.signup.dto.SignupResDto;
import love.shop.web.login.jwt.JwtToken;
import love.shop.web.signup.dto.SignupRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final LoginService loginService;
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;

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
        log.info("로그인 성공");
        return ResponseEntity.ok(new JwtToken(tokenInfo.getGrantType(), tokenInfo.getAccessToken(), null));
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResDto> logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("로그아웃");

        // http 헤더와 쿠키에서 각각 엑세스 토큰과 리프레시 토큰을 꺼내와야함.
        // 컨트롤러에서 http 헤더와 쿠키 뽑는 방법은?

        // 1. 서버에서 로그아웃은 엑세스 토큰과 리프레시 토큰을 블랙 리스트 처리하고,
        // 2. 쿠키에 들어 있는 리프레시 토큰을 제거한다.

        String accessToken = jwtTokenProvider.extractAccessToken(request);
        String refreshToken = jwtTokenProvider.extractRefreshToken(request);

        log.info("request에 리프레시 토큰이 없다구요?={}", refreshToken);

        redisService.addTokenBlackList(accessToken);
        redisService.addTokenBlackList(refreshToken);

        // 쿠키를 만료 시킬때는 response에서 해야함
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok(new LogoutResDto("로그아웃 성공"));
    }

    // 멤버 정보 조회 정보 조회
    @GetMapping("/member/info")
    public ResponseEntity<MemberInfoResDto> memberInfo() {
        // 토큰 안에 있는 memberId로 멤버 조회. 엑세스 토큰에 아예 memberId가 박혀 있다
        // 토큰 정보는 SecurityContextHolder에 있다. 이것 안에 memberId를 가져온다.
        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId();

        log.info("memberId={}", memberId);

        log.info("memberInfo 실행");
        MemberInfoResDto memberInfo = memberService.memberInfo(memberId);
        return ResponseEntity.ok(memberInfo);
    }

    // 현재 로그인 중인지. 그런데 여기서 문제는 페이지를 이동할 때마다 유저 정보를 계속해서 줘야한다는건데.. 이건 나중에 차차 생각하고 일단 구현에 집중하자.
    @GetMapping("/member/islogin")
    public ResponseEntity<IsLoginUserDto> memberIdLogin() {

        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId();

        log.info("memberId값={}", memberId);

        Member member = memberService.findUserById(memberId);
        log.info("현재 로그인 중인 유저={}", member.getName());

        IsLoginUserDto loginMember = new IsLoginUserDto();
        loginMember.setUserName(member.getName());

        return ResponseEntity.ok(loginMember);
    }
}
