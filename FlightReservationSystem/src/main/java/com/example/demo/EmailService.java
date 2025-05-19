package com.example.demo;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendReservationConfirmation(String to, String reservationNumber, String flightNumber, LocalDate departureDate) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        helper.setTo(to);
        helper.setSubject("flightReservationConfirmation");

        String htmlContent = buildHtmlTemplate(reservationNumber, flightNumber, String.valueOf(departureDate));
        helper.setText(htmlContent, true);

        FileSystemResource image1 = new FileSystemResource(new File("src/main/resources/flightReservationConfirmation/img/i1009126258.png"));
        helper.addInline("logoImage", image1);
        FileSystemResource image2 = new FileSystemResource(new File("src/main/resources/flightReservationConfirmation/img/i589725492.png"));
        helper.addInline("planeImage", image2);
        mailSender.send(message);
    }

    private String buildHtmlTemplate(String reservationNumber, String flightNumber, String departureDate) {
        String template = loadHtmlTemplateFromResources();

        return template
                .replace("[NUMER REZ.]", reservationNumber)
                .replace("[NUMER LOTU]", flightNumber)
                .replace("[DATA WYLOTU]", departureDate);
    }

    private String loadHtmlTemplateFromResources() {
        try {
            return new String(new ClassPathResource("flightReservationConfirmation/html-email.html").getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Could not load HTML email template", e);
        }
    }
}