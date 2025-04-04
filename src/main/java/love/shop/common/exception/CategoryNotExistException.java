package love.shop.common.exception;

public class CategoryNotExistException extends RuntimeException {
    public CategoryNotExistException() {
        super();
    }

    public CategoryNotExistException(String message) {
        super(message);
    }

    public CategoryNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public CategoryNotExistException(Throwable cause) {
        super(cause);
    }

    protected CategoryNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
