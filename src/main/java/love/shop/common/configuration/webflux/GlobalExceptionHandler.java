package love.shop.common.configuration.webflux;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GlobalExceptionHandler extends DefaultErrorWebExceptionHandler {


    public GlobalExceptionHandler(ErrorAttributes errorAttributes,
                                  WebProperties resources,
                                  ErrorProperties errorProperties,
                                  ApplicationContext applicationContext,
                                  ServerCodecConfigurer serverCodecConfigurer) {

        super(errorAttributes, resources.getResources(), errorProperties, applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    // 오류 발생 시 어떻게 라우팅할지 정의합니다.
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    // 실제 오류 응답을 생성하는 메소드입니다.
    public Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        log.info("안거치나?");

        Map<String, Object> errorAttributes = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.APPLICATION_JSON)); // options를 가져와 전달

        // 오류 상태 코드를 가져옵니다. (변경 없음)
        int status = Optional.ofNullable(errorAttributes.get("status"))
                .map(Object::toString)
                .map(Integer::parseInt)
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR.value());

        HttpStatus httpStatus = HttpStatus.valueOf(status);

        // 로그 등 필요한 추가 처리를 할 수 있습니다. (변경 없음)
        // System.err.println("Error occurred: " + errorAttributes.get("message"));
        // Throwable actualError = getError(request); // 실제 Throwable 객체 접근

        // 오류 정보를 담은 응답을 생성하고 반환합니다. (변경 없음)
        return ServerResponse.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON) // JSON 형태로 응답
                .body(BodyInserters.fromValue(errorAttributes)); // 오류 정보를 응답 본문에 담기
    }

}
