package com.gsoft.projectManager;

import static org.springframework.test.util.AssertionErrors.assertFalse;

import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;


@SpringBootTest
class ProjectManagerApplicationTests {

	private final JavaMailSender javaMailSender;

	ProjectManagerApplicationTests(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Test
	void contextLoads() {

	}

	@Test
	public void send() throws Exception {
		System.out.println("--------------1------------------");
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            mimeMessage.setContent(mail,"text/html");
		System.out.println("--------------2------------------" + mimeMessage);
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
		System.out.println("--------------3------------------" + helper.toString());
		helper.setText("This is the test mail", false);
		System.out.println("--------------4------------------" + helper.toString());
		helper.setTo("test@mymail.com");
		System.out.println("--------------5------------------");
		helper.setSubject("Email verification from ProjectManager");
		System.out.println("--------------6------------------");
		helper.setFrom("test@developer.com");
		try {
			javaMailSender.send(mimeMessage);
		} catch (MailException e) {
			assertFalse("Email cannot be sent", true);
		}
	}

}
