package com.gsoft.projectManager.mailer;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender{
    private final JavaMailSender javaMailSender;

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Override
    @Async
    public void send(String to, String mail){
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            mimeMessage.setContent(mail,"text/html");
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setText(mail, true);
            helper.setTo(to);
            helper.setSubject("Email verification from ProjectManager");
            helper.setFrom("girish.sakore@skyach.co");
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            LOGGER.error("Failed to send email.", e);
            throw new IllegalStateException("Failed to send email.");
        }
    }
}
