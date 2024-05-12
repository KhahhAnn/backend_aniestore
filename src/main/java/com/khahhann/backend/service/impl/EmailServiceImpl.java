package com.khahhann.backend.service.impl;

import com.khahhann.backend.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Data
@Service
public class EmailServiceImpl implements EmailService {
    private JavaMailSender mailSender;

    @Override
    public void sendEmail(String from, String to, String subject, String text) {
        MimeMessage message = this.mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setFrom(from);
            helper.setSubject(subject);
            helper.setText(text, true);
        } catch (MessagingException messagingException) {
            throw new RuntimeException(messagingException);
        }
        this.mailSender.send(message);
    }
}
