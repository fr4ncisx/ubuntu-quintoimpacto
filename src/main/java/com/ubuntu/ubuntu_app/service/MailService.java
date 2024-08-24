package com.ubuntu.ubuntu_app.service;

import com.ubuntu.ubuntu_app.model.dto.MicrobusinessDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.time.LocalDate;
import java.util.List;

@EnableScheduling
@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MicrobusinessService microbusinessService;

    @Autowired
    private UserService userService;

    @Autowired
    private TemplateEngine templateEngine;

    @Transactional    
    //@Scheduled(cron = "0 0 9 ? * FRI", zone = "America/Argentina/Buenos_Aires") //  https://crontab.cronhub.io/ Generador de expresion Cron
    @Scheduled(cron = "0 */5 * * * *")
    public void prepareNewsMicroBussinessToSend() throws MessagingException {
        List<MicrobusinessDTO> micros = microbusinessService.microsNotSent();
        String[] admins = userService.findAllEmails();
        sendNewMicroBusinessToAdmins(admins, "informe Semanal ubuntu", micros);
    }

    public void sendNewMicroBusinessToAdmins(String[] to, String subject, List<MicrobusinessDTO> micros) throws MessagingException {
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
