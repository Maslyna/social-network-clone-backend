package net.maslyna.notification.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.service.PropertiesMessageService;
import net.maslyna.notification.service.EmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final PropertiesMessageService messageService;

    @Override
    public void sendEmail(String subject, String text, String... to) {
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
    public void sendEmail(String subject, String text, List<String> to) {
        String[] sendTo = to.toArray(new String[0]);
        sendEmail(subject, text, sendTo);
    }
}
