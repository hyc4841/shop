package love.shop.common.exception;

public class AddressNullException extends RuntimeException {
    public AddressNullException() {
        super();
    }

    public AddressNullException(String message) {
        super(message);
    }

    public AddressNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddressNullException(Throwable cause) {
        super(cause);
    }

    protected AddressNullException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
