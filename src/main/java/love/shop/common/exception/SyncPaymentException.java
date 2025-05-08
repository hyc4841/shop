package love.shop.common.exception;

import lombok.Getter;

@Getter
public class SyncPaymentException extends RuntimeException {

    private final String errorCode;
    private final String errorMessage;

    public SyncPaymentException(String errorCode, String errorMessage) {
        super(errorMessage); // 부모 RuntimeException에 메시지를 전달
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
