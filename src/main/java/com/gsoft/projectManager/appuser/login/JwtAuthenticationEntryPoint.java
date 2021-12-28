package com.gsoft.projectManager.appuser.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) 
            throws IOException, ServletException{
        LOGGER.error("Authentication entry point commence");
        e.printStackTrace();
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not authorized to access this resource!");
    }
}
