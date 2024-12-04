package love.shop.web.login.jwt.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    TOKEN_DOES_NOT_EXIST("토큰이 존재하지 않습니다."),
    INVALID_TOKEN_INFO("유효하지 않은 토큰 입니다."),
    TIME_OUT_TOKEN("만료된 토큰입니다.");

    private final String message;

    ExceptionCode(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
