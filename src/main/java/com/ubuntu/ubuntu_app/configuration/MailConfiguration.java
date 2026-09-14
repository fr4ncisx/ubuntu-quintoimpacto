package com.ubuntu.ubuntu_app.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.ubuntu.ubuntu_app.shared.config.EmailProperties;

import lombok.RequiredArgsConstructor;

import java.util.Properties;

@Configuration
@ConditionalOnProperty(name = "app.mail.enabled", havingValue = "true", matchIfMissing = false)
@RequiredArgsConstructor
public class MailConfiguration {

    private final EmailProperties emailProperties;

    @Bean
    public JavaMailSender getJavaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(emailProperties.address());
        mailSender.setPassword(emailProperties.password());

        Properties props =mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol","smtp");
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug","false");

        return mailSender;
    }




}
