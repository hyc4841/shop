package love.shop.common.exception;

public class RefreshTokenNotExistException extends RuntimeException{
    public RefreshTokenNotExistException() {
        super();
    }

    public RefreshTokenNotExistException(String message) {
        super(message);
    }

    public RefreshTokenNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public RefreshTokenNotExistException(Throwable cause) {
        super(cause);
    }

    protected RefreshTokenNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
