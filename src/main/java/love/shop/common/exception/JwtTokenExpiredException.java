package love.shop.common.exception;

public class JwtTokenExpiredException extends RuntimeException {

    public JwtTokenExpiredException() {
        super();
    }

    public JwtTokenExpiredException(String message) {
        super(message);
    }

    public JwtTokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtTokenExpiredException(Throwable cause) {
        super(cause);
    }

    protected JwtTokenExpiredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
