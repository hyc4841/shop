package love.shop.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.common.exception.PasswordNotMatchException;
import love.shop.common.exception.UnauthorizedAccessException;
import love.shop.common.exception.UserNotExistException;
import love.shop.domain.member.Member;
import love.shop.service.login.LoginService;
import love.shop.service.member.MemberService;
import love.shop.web.login.dto.*;
import love.shop.web.member.dto.*;
import love.shop.web.signup.dto.*;
import love.shop.web.login.jwt.JwtToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final LoginService loginService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequestDto signupDto, BindingResult bindingResult) throws MethodArgumentNotValidException {
        log.info("회원가입 시작={}", signupDto);
        // 이메일 인증 확인
        memberService.checkEmailCertification(signupDto.getEmail(), bindingResult);
        // 아이디 중복 확인
        memberService.loginIdDuplicationValidation(signupDto.getLoginId(), bindingResult);
        // 회원가입 진행
        Member member = memberService.signUp(signupDto);

        return ResponseEntity.ok(new MemberDto(member));
    }

    @PostMapping("/signup/auth/loginId")
    public ResponseEntity<?> checkLoginId(@RequestBody @Valid LoginIdCheckReqDto loginIdDto, BindingResult bindingResult) throws MethodArgumentNotValidException {
        log.info("아이디 중복 확인");
        memberService.loginIdDuplicationValidation(loginIdDto.getLoginId(), bindingResult);

        LoginIdCheckResDto response = new LoginIdCheckResDto(loginIdDto.getLoginId(), "사용 가능한 아이디입니다", 200);
        return ResponseEntity.ok(response);
    }


    // 이메일 인증 요청
    @PostMapping("/signup/auth/email")
    public ResponseEntity<?> sendEmailCertification(@RequestBody @Valid EmailCertificationDto emailDto) throws MessagingException, MethodArgumentNotValidException {
        memberService.sendEmailCertification(emailDto.getEmail()); // 인증 코드 전송

        return ResponseEntity.ok("ok");
    }

    @PostMapping("/signup/auth/email/confirm")
    public ResponseEntity<?> ConfirmEmailCertification(@RequestBody @Valid EmailCertificationConfirmReqDto confirmDto, BindingResult bindingResult) throws MethodArgumentNotValidException {
        // 만약 해당 이메일로 인증 코드를 보낸적도 없는데 누군가가 계속해서 요청한다면 어떻게 막을 것인가?
        log.info("이메일 인증 실행");
        memberService.confirmEmailCertification(confirmDto.getEmail(), confirmDto.getCode(), bindingResult);

        EmailCertificationConfirmResDto response = new EmailCertificationConfirmResDto(confirmDto.getEmail(), "이메일 인증에 성공했습니다.", 200);

        return ResponseEntity.ok(response);
    }

    // 로그인 검사에 통과하면 토큰을 발급해준다.
    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginReqDto loginDto, HttpServletResponse response) {
        log.info("로그인 시도={}", loginDto);
        JwtToken tokenInfo = loginService.login(loginDto.getLoginId(), loginDto.getPassword(), response);

        // 로그인 아이디로 회원 찾기
        Member member = memberService.findMemberByLoginId(loginDto.getLoginId()).orElseThrow(() -> new UserNotExistException());

        log.info("로그인 성공");
        return ResponseEntity.ok(new LoginResult<>(tokenInfo, member.getName()));
    }

    @Data
    @AllArgsConstructor
    static class LoginResult<T> {
        private T token;
        private String userName;
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // http 헤더와 쿠키에서 각각 엑세스 토큰과 리프레시 토큰을 꺼내와야함.
        // 컨트롤러에서 http 헤더와 쿠키 뽑는 방법은?

        // 1. 서버에서 로그아웃은 엑세스 토큰과 리프레시 토큰을 블랙 리스트 처리하고,
        // 2. 쿠키에 들어 있는 리프레시 토큰을 제거한다.

        // 쿠키를 만료 시킬때는 response에서 해야함

        log.info("리프레시 토큰 없애주기 실행");
        loginService.logout(response);

        return ResponseEntity.ok(new LogoutResDto("로그아웃 성공"));
    }

    @PostMapping("/member/info/login")
    public ResponseEntity<?> memberInfoLogin(@Validated @RequestBody MemberInfoLoginReqDto loginDto) {
        // 멤버 정보 수정 화면 전 유저 확인용 로그인 창
        // 비밀번호만 확인하고 들어가는 느낌
        // 비밀번호 확인은 필드 검증으로 위임

        return new ResponseEntity<>(HttpStatus.OK);
    }


    // 멤버 정보 조회 정보 조회
    @GetMapping("/member/info")
    public ResponseEntity<?> memberInfo() {
        log.info("memberInfo 실행");

        // 토큰 안에 있는 memberId로 멤버 조회. 엑세스 토큰에 아예 memberId가 박혀 있다
        // 토큰 정보는 SecurityContextHolder에 있다. 이것 안에 memberId를 가져온다.
        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId();
        MemberDto memberInfo = memberService.memberInfo(memberId);

        return ResponseEntity.ok(memberInfo);
    }

    // 비밀번호 변경
    @PostMapping("/member/password")
    public ResponseEntity<?> passwordUpdate(@Validated @RequestBody PasswordUpdateReqDto passwordDto) {
        log.info("비밀번호 변경 시작");
        log.info("passwordDto={}", passwordDto);
//        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId();
        Long memberId = currentUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

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
    public ResponseEntity<?> loginIdUpdate(@RequestBody @Validated LoginIdUpdateReqDto loginIdDto) {
        // 1. 유저가 입력한 아이디가 기존 유저 아이디 중에 중복되는 것이 있는지 확인.
        // 1번은 필드 검증으로 위임
        // 2. 중복 확인이 되면 변경
//        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId();
        Long memberId = currentUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        Member member = memberService.updateLoginId(loginIdDto.getNewLoginId(), memberId);
        MemberDto memberDto = new MemberDto(member);
        MemberInfoResult<MemberDto> result = new MemberInfoResult<>(memberDto);
        // 만약 어떤 두 유저가 같은 아이디로 동시에 바꿀려고 하면 최종적으로는 데이터베이스에서 유니크 제약조건으로 막아줘야 한다.
        return ResponseEntity.ok(result);
    }

    // 이메일 변경
    @PostMapping("/member/email")
    public ResponseEntity<?> emailUpdate(@RequestBody @Validated EmailUpdateReqDto emailDto, BindingResult bindingResult) throws MethodArgumentNotValidException {
        // 0. 이메일 인증. 이메일 인증은 클라이언트 쪽에서 진행해야 할듯? 아니면 서버쪽에서 인증 이메일 보내는거 해줘도 되고?
        // 1. 이메일 형식 검사 (필드 검증으로 위임)
        // 2. 이메일 중복 검사 (필드 검증으로 위임)
        // 3. 이메일 인증 확인
        memberService.checkEmailCertification(emailDto.getNewEmail(), bindingResult);


        // 3. 이메일 변경
//        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId();
        Long memberId = currentUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Member member = memberService.updateEmail(emailDto.getNewEmail(), memberId);

        MemberDto memberDto = new MemberDto(member);
        MemberInfoResult<MemberDto> result = new MemberInfoResult<>(memberDto);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/member/email/auth")
    public ResponseEntity<?> sendUpdateEmailCertification(@RequestBody @Valid EmailCertificationDto emailDto) throws MessagingException {
        Long memberId = currentUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        memberService.sendEmailCertification(emailDto.getEmail());

        return ResponseEntity.ok("ok");
    }

    // 이메일 변경 인증을 완전히 분리해야 할까?
    @PostMapping("/member/email/auth/confirm")
    public ResponseEntity<?> updateEmailCertificationConfirm(@RequestBody @Valid EmailCertificationConfirmReqDto confirmDto,
                                                             BindingResult bindingResult) throws MethodArgumentNotValidException {

        log.info("인증 코드 확인 ={}", confirmDto.getCode());
        log.info("인증 이메일 확인 ={}", confirmDto.getEmail());

        Long memberId = currentUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        memberService.confirmEmailCertification(confirmDto.getEmail(), confirmDto.getCode(), bindingResult);

        EmailCertificationConfirmResDto response = new EmailCertificationConfirmResDto(confirmDto.getEmail(), "인증에 성공했습니다.", 200);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/member/name")
    public ResponseEntity<?> nameUpdate(@Validated @RequestBody NameUpdateReqDto nameDto) {

        // 1. 본인인증??
        // 2. 별도의 검증 과정 없이 일단 이름은 변경시키는 걸로
//        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId();
        Long memberId = currentUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal());


        Member member = memberService.updateName(nameDto.getNewName(), memberId);
        MemberDto memberDto = new MemberDto(member);
        MemberInfoResult<MemberDto> result = new MemberInfoResult<>(memberDto);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/member/phone-num")
    public ResponseEntity<?> phoneNumUpdate(@Validated @RequestBody PhoneNumUpdateReqDto phoneNumDto) {

        // 전화번호는 인증과정을 거쳐야한다.
        // 1. 본인인증??
        // 2. 별도의 검증 과정 없이 일단 이름은 변경시키는 걸로
//        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId();
        Long memberId = currentUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal());


        Member member = memberService.updatePhoneNum(phoneNumDto.getNewPhoneNum(), memberId);
        MemberDto memberDto = new MemberDto(member);
        MemberInfoResult<MemberDto> result = new MemberInfoResult<>(memberDto);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/member/address")
    public ResponseEntity<?> addressUpdate(@Validated @RequestBody AddressUpdateReqDto addressDto) {
//        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId();
        Long memberId = currentUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        log.info("addressDto={}", addressDto);

        Member member = memberService.updateAddress(addressDto, memberId);

        MemberDto memberDto = new MemberDto(member);
        MemberInfoResult<MemberDto> result = new MemberInfoResult<>(memberDto);

        return ResponseEntity.ok(result);
    }


    // 회원 정보 반환 데이터
    @Data
    @AllArgsConstructor
    static class MemberInfoResult<T> {
        private T memberInfo;
    }

    @GetMapping("/member/isLogin")
    public ResponseEntity<?> memberIdLogin() {
        Long memberId = currentUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
//        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId();
        Member member = memberService.findMemberById(memberId);
        log.info("현재 로그인 중인 유저={}", member.getName());

        IsLoginUserDto loginMember = new IsLoginUserDto(member.getName());

        return ResponseEntity.ok(loginMember);
    }

    // 리프레시 토큰으로 엑세스 토큰 재발급 받는 테스트
    @CrossOrigin(exposedHeaders = "Access-Token")
    @GetMapping("/refresh")
    public ResponseEntity<ReAccessToken> reGenerateAccessToken(HttpServletResponse response) {
        ReAccessToken reAccessToken = new ReAccessToken("엑세스 토큰 재발급 성공?");
        return ResponseEntity.ok(reAccessToken);
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        log.info("SecurityContextHolder 테스트");
//        Long memberId = ((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId();
//        CustomUser principal = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

//        log.info("로그인 안한 상태에서 SecurityContextHolder에 있는 멤버 정보 빼오기={}", memberId);
//        log.info("이건 뭘까?={}", principal);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("이건 뭘까?={}", principal);

        if (principal.getClass() == String.class && principal == "anonymousUser") {
            log.info("true");
        } else {
            log.info("false");
        }

        return ResponseEntity.ok("ok");
    }

    private Long currentUser(Object principal) {
        // 그런데 SecurityContextHolder로 하는거 말고도 HttpServletRequest로 하는 방법도 있음.
        log.info("현재 로그인 중인지 아닌지 검사 실행");
        try {
            if (principal.getClass() == CustomUser.class) {
                return ((CustomUser) principal).getMemberId();
            }
            throw new UnauthorizedAccessException();
        } catch (Error error) {
            throw new UnauthorizedAccessException(error);
        }
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
