package net.thrymr.task.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.thrymr.task.custom.exception.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest req,
                       HttpServletResponse res,
                       AccessDeniedException accessDeniedException) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setStatus(HttpStatus.FORBIDDEN.value());
        res.getWriter().write(mapper.writeValueAsString(new ApiResponse(HttpStatus.FORBIDDEN.value(), "Access denied")
        ));
    }
}