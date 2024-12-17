package love.shop.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice // RestControllerAdvice는 주로 컨트롤러에서 발생하는 예외를 처리해준다.
// 필터같은 경우는 컨트롤러 보다 먼저 실행되기 때문에 RestControllerAdvice의 범위에 해당하지 않는다.

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
        log.error("[exceptionHandle] ex", e); // 지금 이건 일부러 로그에 오류가 뜨도록 만든것
        return new ErrorApi(500, "아이디 혹은 비밀번호가 맞지 않습니다.");
    }


}
