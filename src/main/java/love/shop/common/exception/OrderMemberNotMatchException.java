package love.shop.common.exception;

public class OrderMemberNotMatchException extends RuntimeException {

    public OrderMemberNotMatchException() {
        super();
    }

    public OrderMemberNotMatchException(String message) {
        super(message);
    }

    public OrderMemberNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderMemberNotMatchException(Throwable cause) {
        super(cause);
    }

    protected OrderMemberNotMatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
