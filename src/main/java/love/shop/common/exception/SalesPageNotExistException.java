package love.shop.common.exception;

public class SalesPageNotExistException extends RuntimeException {
    public SalesPageNotExistException() {
        super();
    }

    public SalesPageNotExistException(String message) {
        super(message);
    }

    public SalesPageNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public SalesPageNotExistException(Throwable cause) {
        super(cause);
    }

    protected SalesPageNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
