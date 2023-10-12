package net.maslyna.notification.service;

public interface EmailService {
    void sendEmail(String subject, String text, String to);

    void sendEmail(String subject, String text, String... to);
}
