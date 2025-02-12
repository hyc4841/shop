package love.shop.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorWrapper<ErrorApi> userIdOrPasswordNotMatchExHandler(UserIdOrPasswordNotMatchException e) {
        log.error("[exceptionHandle] ex", e); // 지금 이건 일부러 로그에 오류가 뜨도록 만든것

        ErrorWrapper<ErrorApi> errorApiErrorWrapper = new ErrorWrapper<>(new ErrorApi(500, "아이디 혹은 비밀번호가 맞지 않습니다."));

        return errorApiErrorWrapper;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> signupValidationExHandler(MethodArgumentNotValidException e, WebRequest request) {
        log.info("회원가입 데이터 검증 실패");
        HashMap<String, String> errors = new HashMap<>();
        errors.put("message", "회원가입 정보를 정확히 입력해주세요.");
        // 아래와 같이 필요에 따라 필드 오류를 담아서 보낼 수 있음.
        e.getBindingResult().getFieldErrors().forEach(error -> {
            log.info("에러 이름={}, 에러 내용={}", error.getField(), error.getDefaultMessage());
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(RefreshTokenNotExistException.class)
    public ErrorApi refreshTokenNotExistExHandler(RefreshTokenNotExistException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorApi(401, "redis에 리프레시 토큰이 없음.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PasswordNotMatchException.class)
    public ErrorApi passwordNotMatchExHandler(PasswordNotMatchException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorApi(400, "비밀번호가 일치하지 않습니다.");
    }

    @Data
    @AllArgsConstructor
    static class ErrorWrapper<T> {
        private T error;
    }
}
