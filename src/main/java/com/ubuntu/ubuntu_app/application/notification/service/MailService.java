package com.ubuntu.ubuntu_app.application.notification.service;

import com.ubuntu.ubuntu_app.application.microbusiness.port.in.MicrobusinessUseCase;
import com.ubuntu.ubuntu_app.application.user.port.in.UserUseCase;
import com.ubuntu.ubuntu_app.application.microbusiness.api.CreateMicrobusinessRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.mail.enabled", havingValue = "true", matchIfMissing = false)
public class MailService {

    private final JavaMailSender mailSender;

    private final MicrobusinessUseCase microbusinessService;

    private final UserUseCase userService;

    private final TemplateEngine templateEngine;

    @Transactional
    @Scheduled(cron = "0 0 8 ? * FRI", zone = "America/Argentina/Buenos_Aires") //  https://crontab.cronhub.io/ Generador de expresion Cron
    public void sendWeeklyNewsletter() throws MessagingException {
        List<CreateMicrobusinessRequest> micros = microbusinessService.findUnmailed();
        String[] admins = userService.findAdminEmails();
        if (admins == null || admins.length == 0) {
            return;
        }
        sendToAdmins(admins, "Informe Semanal Ubuntu", micros);
    }

    public void sendToAdmins(String[] to, String subject, List<CreateMicrobusinessRequest> micros) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(to);
        helper.setSubject(subject + " " + LocalDate.now());

        Context context = new Context();
        context.setVariable("micros", micros);

        String contenido = templateEngine.process("mailTemplate", context);
        helper.setText(contenido, true);

        mailSender.send(mimeMessage);
    }
}
