package com.sinaukoding.eventbooking.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinaukoding.eventbooking.model.response.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AccessDeniedConfig implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        BaseResponse<?> errorResponse = BaseResponse.forbiddenAccess("You don’t have permission to access this resource");

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

}
