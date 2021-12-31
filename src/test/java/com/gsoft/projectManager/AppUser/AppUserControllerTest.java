package com.gsoft.projectManager.AppUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.gsoft.projectManager.appuser.AppUserRepository;
import com.gsoft.projectManager.appuser.AppUserService;
import com.gsoft.projectManager.appuser.UserPrincipal;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters=false)
public class AppUserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private static AppUserRepository appUserRepository;

    @Autowired
    private Gson gson;

    private final String URL_PREFIX = "/api/v1/appUsers";
    private final Logger LOGGER = LoggerFactory.getLogger(AppUserControllerTest.class);

    @BeforeAll
    public static void init() throws Exception{
        // TODO : user repo not working here, need to get it straight from the web context
        UserPrincipal user = UserPrincipal.create(appUserRepository.findByUsername("lewton").orElseThrow(()-> new Exception("User not found!")));
        System.out.println("=================user = " + user.toString());
        Authentication authentication = new UsernamePasswordAuthenticationToken("lewton", "123456789", user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void checkAllAppUsers() throws Exception{
        String expectedResult = gson.toJson(appUserService.getAllAppUsers());
        LOGGER.info("=========================================");
        LOGGER.info(expectedResult);
        mockMvc.perform(get(URL_PREFIX + "/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedResult));
        // mockMvc.perform(get(URL_PREFIX + "/"))
        //         .andExpect(status().isUnauthorized());
    }
}
