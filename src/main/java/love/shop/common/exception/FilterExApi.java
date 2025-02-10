package love.shop.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FilterExApi {

    // 필터에서 발생한 토큰 예외를 api로 처리하기 위한 함수
    public void jwtTokenExHandler(HttpServletResponse response, String message, int code) {
        response.setStatus(code);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8"); // 인코딩 안해주면 한글 안나옴
        try {
            ErrorApi errorApi = new ErrorApi(code, message);
            // 에러 데이터를 한번 감싸서 보낸다.
            String api = new ObjectMapper().writeValueAsString(new jwtExResult<>(errorApi));
            response.getWriter().write(api);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Data
    @AllArgsConstructor
    static class jwtExResult<T> {
        private T error;
    }

}
