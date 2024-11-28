package love.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.User;
import love.shop.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestApiController {

    private final UserService userService;

    @GetMapping("/api/user")
    public List<User> findAllUser() {
        log.info("실행");
        return userService.findAllUser();
    }
}
