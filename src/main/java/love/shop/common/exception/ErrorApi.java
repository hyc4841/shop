package love.shop.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorApi {
    private int status;
    private String message;
//    private Object object;
}
