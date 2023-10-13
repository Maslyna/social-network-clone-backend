package net.maslyna.notification.service;

import java.util.List;

public interface EmailService {

    void sendEmail(String subject, String text, String... to);

    void sendEmail(String subject, String text, List<String> to);
}
