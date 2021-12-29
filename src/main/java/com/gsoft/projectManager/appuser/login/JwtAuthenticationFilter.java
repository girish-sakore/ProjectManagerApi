package com.gsoft.projectManager.appuser.login;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gsoft.projectManager.appuser.AppUser;
import com.gsoft.projectManager.appuser.AppUserRepository;
import com.gsoft.projectManager.appuser.AppUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
    private JwtTokenProvider jwtTokenProvider;
    private AppUserRepository appUserRepository;
    private AppUserService appUserService;

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {

        try{
            initBeans(request);    
            String jwtToken = getJwtToken(request.getHeader("Authorization"));
            
            if(jwtToken != null && jwtTokenProvider.validateToken(jwtToken)){
                Long userId = jwtTokenProvider.getUserIdFromJwtToken(jwtToken);
                Optional<AppUser> optionalAppUser = appUserRepository.findById(userId);
                appUserService.loginUser(optionalAppUser);
            }
        } catch(Exception e){
            e.printStackTrace();
            LOGGER.error("Unable to authenticate user using jwt token");
        }
    
        filterChain.doFilter(request, response);
    }

    private String getJwtToken(String header){
        if(header != null && header.startsWith("Bearer ")){
            return header.substring(7);
        }
        return null;
    }
    
    private void initBeans(HttpServletRequest request){
        ServletContext servletContext = request.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        jwtTokenProvider = webApplicationContext.getBean(JwtTokenProvider.class);
        appUserRepository = webApplicationContext.getBean(AppUserRepository.class);
        appUserService = webApplicationContext.getBean(AppUserService.class);
    }
}
