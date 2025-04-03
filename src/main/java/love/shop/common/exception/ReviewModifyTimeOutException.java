package love.shop.common.exception;

public class ReviewModifyTimeOutException extends RuntimeException {
    public ReviewModifyTimeOutException() {
        super();
    }

    public ReviewModifyTimeOutException(String message) {
        super(message);
    }

    public ReviewModifyTimeOutException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReviewModifyTimeOutException(Throwable cause) {
        super(cause);
    }

    protected ReviewModifyTimeOutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
