package love.shop.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class FilterExApi {

    // 필터에서 발생한 토큰 예외를 api로 처리하기 위한 함수
    public void JwtTokenExHandler(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8"); // 인코딩 안해주면 한글 안나옴
        try {
            String api = new ObjectMapper().writeValueAsString(new ErrorApi(401, message));
            response.getWriter().write(api);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }






}
