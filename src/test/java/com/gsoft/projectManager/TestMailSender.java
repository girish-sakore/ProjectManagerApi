package com.gsoft.projectManager;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest
public class TestMailSender {

    // @Autowired
    // private JavaMailSender javaMailSender;

    // @Test
	// public void send() throws Exception {
	// 	System.out.println("--------------1------------------");
    //     try{
    //         MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    //         //            mimeMessage.setContent(mail,"text/html");
    //         System.out.println("--------------2------------------" + mimeMessage);
    //         MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
    //         System.out.println("--------------3------------------" + helper.toString());
    //         helper.setText("This is the test mail", false);
    //         System.out.println("--------------4------------------" + helper.toString());
    //         helper.setTo("test@mymail.com");
    //         System.out.println("--------------5------------------");
    //         helper.setSubject("Email verification from ProjectManager");
    //         System.out.println("--------------6------------------");
    //         helper.setFrom("test@developer.com");
    //         try {
    //             javaMailSender.send(mimeMessage);
    //         } catch (MailException e) {
    //             assertFalse(true, "Email cannot be sent");
    //         }
    //     } catch (Exception e){
    //         e.printStackTrace();
    //     }
  
	// }
}
