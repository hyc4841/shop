package love.shop.common.exception;

public class UserSignupDataException extends RuntimeException {

    public UserSignupDataException() {
        super();
    }

    public UserSignupDataException(String message) {
        super(message);
    }

    public UserSignupDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserSignupDataException(Throwable cause) {
        super(cause);
    }

    protected UserSignupDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
