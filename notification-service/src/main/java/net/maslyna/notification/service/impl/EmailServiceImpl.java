package net.maslyna.notification.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.service.PropertiesMessageService;
import net.maslyna.notification.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final PropertiesMessageService messageService;

    @Override
    public void sendEmail(String subject, String text, String to) {
        javaMailSender.send(mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
            mimeMessageHelper.setFrom(messageService.getProperty("spring.mail.username"));
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, true);

            mimeMessage.setHeader("X-Priority", "1");
        });
    }


    @Override
    public void sendEmail(String subject, String text, String... to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(messageService.getProperty("spring.mail.username"));
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }
}
