package love.shop.common.configuration.webflux;

import love.shop.common.exception.SyncPaymentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    private static final Logger log = LoggerFactory.getLogger(CustomErrorAttributes.class);

    // DefaultErrorAttributes의 getErrorAttributes 메소드를 오버라이드합니다.
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {

        log.info("안거치나?");

        // 기본 ErrorAttributes에서 제공하는 정보를 가져옵니다.
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);

        // 오류 객체를 가져옵니다.
        Throwable error = getError(request);

        // 만약 커스텀 예외 타입이라면 추가 정보를 Map에 추가합니다.
        if (error instanceof SyncPaymentException) {
            SyncPaymentException customException = (SyncPaymentException) error;
            errorAttributes.put("errorCode", customException.getErrorCode());
            errorAttributes.put("message", customException.getErrorMessage());
            // 기본 message 필드를 커스텀 메시지로 덮어쓸 수도 있습니다.
            // errorAttributes.put("message", customException.getErrorMessage());
        } else if (error instanceof ResponseStatusException) {
            ResponseStatusException rse = (ResponseStatusException) error;
            errorAttributes.put("status", rse.getStatusCode().value());
            errorAttributes.put("error", rse.getReason());
            errorAttributes.put("message", rse.getReason());
        } else {
            // 그 외 예외는 일반적인 처리를 따르거나, 필요한 기본 정보를 유지합니다.
            // 여기서는 기본 message를 유지합니다.
            errorAttributes.put("message", error.getMessage());
        }

        // 필요한 경우 다른 정보 (예: timestamp 포맷 변경, path 추가 등)를 수정할 수 있습니다.
        // errorAttributes.remove("trace"); // 스택 트레이스를 제거하고 싶을 때 사용

        return errorAttributes;
    }



}
