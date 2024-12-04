package love.shop.common.exception;

public class UserDuplicationException extends RuntimeException {
    public UserDuplicationException() {
        super();
    }

    public UserDuplicationException(String message) {
        super(message);
    }

    public UserDuplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDuplicationException(Throwable cause) {
        super(cause);
    }

    protected UserDuplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
