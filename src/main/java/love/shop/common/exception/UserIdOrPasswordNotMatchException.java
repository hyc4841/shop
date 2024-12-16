package love.shop.common.exception;

public class UserIdOrPasswordNotMatchException extends RuntimeException {

    public UserIdOrPasswordNotMatchException() {
        super();
    }

    public UserIdOrPasswordNotMatchException(String message) {
        super(message);
    }

    public UserIdOrPasswordNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserIdOrPasswordNotMatchException(Throwable cause) {
        super(cause);
    }

    protected UserIdOrPasswordNotMatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
