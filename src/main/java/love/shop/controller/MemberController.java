package love.shop.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.common.exception.PasswordNotMatchException;
import love.shop.common.exception.UserNotExistException;
import love.shop.domain.member.Member;
import love.shop.repository.member.MemberRepository;
import love.shop.service.login.LoginService;
import love.shop.service.member.MemberService;
import love.shop.service.RedisService;
import love.shop.web.login.dto.*;
import love.shop.web.login.jwt.JwtTokenProvider;
import love.shop.web.member.dto.*;
import love.shop.web.signup.dto.SignupResponseDto;
import love.shop.web.login.jwt.JwtToken;
import love.shop.web.signup.dto.SignupRequestDto;
import love.shop.web.test.TestResV1;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final LoginService loginService;
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody @Valid SignupRequestDto signupDto) {
        log.info("회원가입 시작={}", signupDto);
        Long memberId = memberService.signUp(signupDto);

        // 회원가입 성공한 멤버 데이터베이스에서 다시 꺼내서 확인
        Member member = memberService.findMemberById(memberId);

        log.info("회원가입 성공={}", member);

        return ResponseEntity.ok(new SignupResponseDto(200, "회원가입 성공", member));
    }

    // 로그인 검사에 통과하면 토큰을 발급해준다.
    @PostMapping("/login")
    public ResponseEntity<LoginResult<JwtToken>> login(@Validated @RequestBody LoginReqDto loginDto, HttpServletResponse response) {
        log.info("로그인 시도={}", loginDto);

        JwtToken tokenInfo = loginService.login(loginDto.getLoginId(), loginDto.getPassword(), response);
        List<Member> loginMember = memberService.findMemberByLoginId(loginDto.getLoginId());
        if (loginMember.isEmpty()) {
            throw new UserNotExistException("존재하지 않은 유저 입니다.");
        }

        Member member = loginMember.get(0);

        log.info("로그인 성공");
        return ResponseEntity.ok(new LoginResult<JwtToken>(tokenInfo, member.getName()));
    }

    @Data
    @AllArgsConstructor
    static class LoginResult<T> {
        private T token;
        private String userName;
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<LogoutResDto> logout(HttpServletRequest request, HttpServletResponse response) {
        // http 헤더와 쿠키에서 각각 엑세스 토큰과 리프레시 토큰을 꺼내와야함.
        // 컨트롤러에서 http 헤더와 쿠키 뽑는 방법은?

        // 1. 서버에서 로그아웃은 엑세스 토큰과 리프레시 토큰을 블랙 리스트 처리하고,
        // 2. 쿠키에 들어 있는 리프레시 토큰을 제거한다.

        // 쿠키를 만료 시킬때는 response에서 해야함
        log.info("리프레시 토큰 없애주기 실행");
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        log.info("로그아웃 성공");

        return ResponseEntity.ok(new LogoutResDto("로그아웃 성공"));
    }

    @PostMapping("/member/info/login")
    public ResponseEntity<Void> memberInfoLogin(@Validated @RequestBody MemberInfoLoginReqDto loginDto) {
        // 멤버 정보 수정 화면 전 유저 확인용 로그인 창
        // 비밀번호만 확인하고 들어가는 느낌
        // 비밀번호 확인은 필드 검증으로 위임

        return new ResponseEntity<>(HttpStatus.OK);
    }


    // 멤버 정보 조회 정보 조회
    @GetMapping("/member/info")
    public ResponseEntity<MemberDto> memberInfo() {
        // 토큰 안에 있는 memberId로 멤버 조회. 엑세스 토큰에 아예 memberId가 박혀 있다
        // 토큰 정보는 SecurityContextHolder에 있다. 이것 안에 memberId를 가져온다.
        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId();

        log.info("memberId={}", memberId);

        log.info("memberInfo 실행");
        MemberDto memberInfo = memberService.memberInfo(memberId);

        log.info(String.valueOf(memberInfo));
        return ResponseEntity.ok(memberInfo);
    }

    // 비밀번호 변경
    @PostMapping("/member/password")
    public ResponseEntity<MemberInfoResult<MemberDto>> passwordUpdate(@Validated @RequestBody PasswordUpdateReqDto passwordDto) {
        log.info("비밀번호 변경 시작");
        log.info("passwordDto={}", passwordDto);
        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId();
        // 클라이언트 쪽에서 한번 확인하고, 서버에서도 한번더 확인
        // 1. 현재 비밀번호를 꺼내와서 맞는지 확인
        // 1번은 필드 검증으로 위임
        // 2. 변경할 비밀번호와 확인 비밀번호가 맞는지 확인.

        if (!passwordDto.getNewPwd().equals(passwordDto.getNewPwdCon())) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 최종적으로 변경할 비밀번호를 데이터이스에 저장
        Member member = memberService.updatePassword(passwordDto.getNewPwd(), memberId);
        MemberDto memberDto = new MemberDto(member);
        MemberInfoResult<MemberDto> result = new MemberInfoResult<>(memberDto);

        return ResponseEntity.ok(result);
    }

    // 아이디 변경
    @PostMapping("/member/id")
    public ResponseEntity<MemberInfoResult<MemberDto>> loginIdUpdate(@Validated @RequestBody LoginIdUpdateReqDto loginIdDto) {
        // 1. 유저가 입력한 아이디가 기존 유저 아이디 중에 중복되는 것이 있는지 확인.
        // 1번은 필드 검증으로 위임
        // 2. 중복 확인이 되면 변경
        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId();
        Member member = memberService.updateLoginId(loginIdDto.getNewLoginId(), memberId);
        MemberDto memberDto = new MemberDto(member);
        MemberInfoResult<MemberDto> result = new MemberInfoResult<>(memberDto);
        // 만약 어떤 두 유저가 같은 아이디로 동시에 바꿀려고 하면 최종적으로는 데이터베이스에서 유니크 제약조건으로 막아줘야 한다.
        return ResponseEntity.ok(result);
    }

    // 이메일 변경
    @PostMapping("/member/email")
    public ResponseEntity<MemberInfoResult<MemberDto>> emailUpdate(@Validated @RequestBody EmailUpdateReqDto emailDto) {
        // 0. 이메일 인증. 이메일 인증은 클라이언트 쪽에서 진행해야 할듯? 아니면 서버쪽에서 인증 이메일 보내는거 해줘도 되고?
        // 1. 이메일 형식 검사 (필드 검증으로 위임)
        // 2. 이메일 중복 검사 (필드 검증으로 위임)
        // 3. 이메일 변경
        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId();
        Member member = memberService.updateEmail(emailDto.getNewEmail(), memberId);
        MemberDto memberDto = new MemberDto(member);
        MemberInfoResult<MemberDto> result = new MemberInfoResult<>(memberDto);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/member/name")
    public ResponseEntity<MemberInfoResult<MemberDto>> nameUpdate(@Validated @RequestBody NameUpdateReqDto nameDto) {

        // 1. 본인인증??
        // 2. 별도의 검증 과정 없이 일단 이름은 변경시키는 걸로
        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId();

        Member member = memberService.updateName(nameDto.getNewName(), memberId);
        MemberDto memberDto = new MemberDto(member);
        MemberInfoResult<MemberDto> result = new MemberInfoResult<>(memberDto);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/member/phone-num")
    public ResponseEntity<MemberInfoResult<MemberDto>> phoneNumUpdate(@Validated @RequestBody PhoneNumUpdateReqDto phoneNumDto) {

        // 전화번호는 인증과정을 거쳐야한다.
        // 1. 본인인증??
        // 2. 별도의 검증 과정 없이 일단 이름은 변경시키는 걸로
        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId();

        Member member = memberService.updatePhoneNum(phoneNumDto.getNewPhoneNum(), memberId);
        MemberDto memberDto = new MemberDto(member);
        MemberInfoResult<MemberDto> result = new MemberInfoResult<>(memberDto);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/member/address")
    public ResponseEntity<MemberInfoResult<MemberDto>> addressUpdate(@Validated @RequestBody AddressUpdateReqDto addressDto) {
        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId();
        log.info("addressDto={}", addressDto);

        Member member = memberService.updateAddress(addressDto, memberId);

        MemberDto memberDto = new MemberDto(member);
        MemberInfoResult<MemberDto> result = new MemberInfoResult<>(memberDto);

        return ResponseEntity.ok(result);
    }


    @Data
    @AllArgsConstructor
    static class MemberInfoResult<T> {
        private T memberInfo;
    }

    // 현재 로그인 중인지. 그런데 여기서 문제는 페이지를 이동할 때마다 유저 정보를 계속해서 줘야한다는건데.. 이건 나중에 차차 생각하고 일단 구현에 집중하자.
    @GetMapping("/member/islogin")
    public ResponseEntity<IsLoginUserDto> memberIdLogin() {

        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId();

        log.info("memberId값={}", memberId);

        Member member = memberService.findMemberById(memberId);
        log.info("현재 로그인 중인 유저={}", member.getName());

        IsLoginUserDto loginMember = new IsLoginUserDto();
        loginMember.setUserName(member.getName());

        return ResponseEntity.ok(loginMember);
    }

    @GetMapping("/members/test")
    public ResponseEntity<List<MemberDto>> querydslTest() {
        String name = "황윤철";
        List<Member> members = memberRepository.findMembersBySearch(name); // Querydsl로 찾아온 데이터임

        List<MemberDto> memberDtos = new ArrayList<>();

        for (Member member : members) {
            MemberDto memberDto = new MemberDto(member);
            memberDtos.add(memberDto);
        }

        return ResponseEntity.ok(memberDtos);
    }


    // 리프레시 토큰으로 엑세스 토큰 재발급 받는 테스트
    @CrossOrigin(exposedHeaders = "Access-Token")
    @GetMapping("/refresh")
    public ResponseEntity<ReAccessToken> reGenerateAccessToken(HttpServletResponse response) {
        ReAccessToken reAccessToken = new ReAccessToken("엑세스 토큰 재발급 성공?");
        return ResponseEntity.ok(reAccessToken);
    }

    /*
    @GetMapping("/test-v1")
    public TestResV1 testV1() {
        log.info("테스트");
        TestResV1 test = new TestResV1("이것은 테스트입니다.");
        return test;
    }

    @GetMapping("/test-v2")
    public ResponseEntity<TestResV1> testV2() {
        log.info("테스트");
        TestResV1 test = new TestResV1("이것은 테스트입니다.");
        return ResponseEntity.ok(test);
    }

    @GetMapping("/test-v3")
    public ResponseEntity<Map<String, String>> testV3() {
        Map<String, String> map = new HashMap<>();
        map.put("1번", "1번 응답");
        map.put("2번", "2번 응답");

        return ResponseEntity.ok(map);
    }

    @GetMapping("/test-v4")
    public ResponseEntity<List<TestResV1>> testV4() {
        List<TestResV1> list = new ArrayList<>();
        TestResV1 data1 = new TestResV1("1번");
        TestResV1 data2 = new TestResV1("2번");
        list.add(data1);
        list.add(data2);

        return ResponseEntity.ok(list);
    }

    @GetMapping("/test-v5")
    public ResponseEntity<ApiWrapper> testV5() {
        List<TestResV1> list = new ArrayList<>();
        TestResV1 data1 = new TestResV1("1번");
        TestResV1 data2 = new TestResV1("2번");
        list.add(data1);
        list.add(data2);

        ApiWrapper<List<TestResV1>> result = new ApiWrapper<>(list, list.size(), "이것은 테스트입니다");

        return ResponseEntity.ok(result);
    }

    @GetMapping("/test-v6")
    public ResponseEntity<ApiWrapper> testV6() {
        Map<String, String> map = new HashMap<>();
        map.put("1번", "1번 응답");
        map.put("2번", "2번 응답");

        ApiWrapper<Map<String, String>> result = new ApiWrapper<>(map, map.size(), "이것은 테스트입니다.");

        return ResponseEntity.ok(result);
    }


    @Data
    @AllArgsConstructor
    private static class ApiWrapper<T> {
        private T data;
        private int count;
        private String message;
    }

     */
}
