package love.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.member.Member;
import love.shop.service.MemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestApiController {

    private final MemberService memberService;

    @GetMapping("/api/user")
    public List<Member> findAllUser() {
        log.info("실행");
        return memberService.findAllUser();
    }
}
