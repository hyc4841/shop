package love.shop.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExControllerAdvice {
    // Api 예외처리 하는 곳

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorApi userDuplicationExHandler(UserDuplicationException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorApi(400, "이미 등록한 ID가 있습니다.");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorApi userIdOrPasswordNotMatchExHandler(UserIdOrPasswordNotMatchException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorApi(500, "아이디 혹은 비밀번호가 맞지 않습니다.");
    }



}
