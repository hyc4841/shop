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
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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

    // 이게 지금 회원가입 필드 검증할때 뿐만 아니라 다른 곳에서 필드 검증할때도 사용되기 때문에 약간의 수정이 필요함.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> fieldValidationExHandler(MethodArgumentNotValidException e, WebRequest request) {
        log.info("데이터 검증 실패");
        AtomicInteger i = new AtomicInteger(0); // 동시성 제어할 때 사용한다는데 잘 모르겠음
        Map<String, List<ErrorMessages>> errors = new HashMap<>();
        List<ErrorMessages> message = new ArrayList<>();
        message.add(new ErrorMessages(1, "필드에 값을 정확히 입력해주세요."));
        errors.put("message", message);

        // 아래와 같이 필요에 따라 필드 오류를 담아서 보낼 수 있음.
        e.getBindingResult().getFieldErrors().forEach(error -> {
            log.info("필드 이름={}, 에러 내용={}", error.getField(), error.getDefaultMessage());

            if (errors.get(error.getField()) == null) {
                errors.put(error.getField(), new ArrayList<>());
                errors.get(error.getField()).add(new ErrorMessages(errors.get(error.getField()).size() + 1, error.getDefaultMessage()));

            } else {
                errors.get(error.getField()).add(new ErrorMessages(errors.get(error.getField()).size() + 1, error.getDefaultMessage()));
            }
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @Data
    @AllArgsConstructor
    static class ErrorMessages {
        private int id;
        private String message;
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

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ErrorApi unauthorizedAccessException(UnauthorizedAccessException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorApi(403, "접근 권한이 없는 사용자입니다.");
    }

    // 현재 로그인 중인 멤버와 주문 멤버가 다른 경우
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(OrderMemberNotMatchException.class)
    public ErrorApi orderMemberNotMatchException(OrderMemberNotMatchException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorApi(401, "주문 취소는 주문 당사자가 해야합니다. 당신 누구야.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ReviewModifyTimeOutException.class)
    public ErrorApi reviewModifyTimeOutException(ReviewModifyTimeOutException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorApi(400, "리뷰 작성 후 30분이 지나 수정할 수 없습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CategoryNotExistException.class)
    public ErrorApi categoryNotExistException(CategoryNotExistException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorApi(400, "해당 카테고리는 존재하지 않습니다.");
    }


    /*
    @ExceptionHandler(SyncPaymentException.class)
    public Mono<ResponseEntity<ErrorApi>> syncPaymentException(SyncPaymentException e) {
        log.error("[exceptionHandle] ex 여기?", e);
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorApi(400, "결제 검증 실패.")));
    }

     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SyncPaymentException.class)
    public ErrorApi syncPaymentException(SyncPaymentException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorApi(400, "결제 검증 오류 발생");
    }




}
