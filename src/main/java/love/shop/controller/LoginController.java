package love.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.service.LoginService;
import love.shop.service.UserService;
import love.shop.web.login.dto.LoginDto;
import love.shop.web.login.jwt.JwtToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@ResponseBody LoginDto loginDto) {
        loginService.login(loginDto.getUserId(), loginDto.getPassword());
    }
}
