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

    public void JwtTokenExHandler(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String api = new ObjectMapper().writeValueAsString(new ErrorApi(401, "토큰이 만료되었습니다"));
            response.getWriter().write(api);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }




}
