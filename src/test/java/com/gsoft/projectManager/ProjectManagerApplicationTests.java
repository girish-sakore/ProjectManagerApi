package com.gsoft.projectManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.gsoft.projectManager.appuser.AppUserController;
import com.gsoft.projectManager.appuser.login.LoginController;
import com.gsoft.projectManager.registration.RegisterUserController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProjectManagerApplicationTests {
	
	@Autowired
	private AppUserController appUserController;

	@Autowired
	private LoginController loginController;

	@Autowired
	private RegisterUserController registerUserController;

	@Test
	void contextLoads() {
		assertNotNull(appUserController);
		assertNotNull(loginController);
		assertNotNull(registerUserController);
	}
}
