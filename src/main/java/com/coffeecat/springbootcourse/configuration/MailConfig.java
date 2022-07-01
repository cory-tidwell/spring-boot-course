package com.coffeecat.springbootcourse.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
    // From application.properties:
//    mail.smtp.host = "smtp.mailtrap.io"
//    mail.smtp.port = 2525
//    mail.smtp.user = "fdb3336e38e191"
//    mail.smtp.password = "2039cf7ea15b5c"

    @Value("${mail.smtp.host}") //allows specifying value to assign to field:
    private String host;

    @Value("${mail.smtp.port}")
    private int port;

    @Value("${mail.smtp.user}")
    private String user;

    @Value("${mail.smtp.password}")
    private String password;

    @Bean //make JavaMailSender Method available for @Autowired in other classes.
    public JavaMailSender javaMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        //set ports,users etc:
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(user);
        mailSender.setPassword(password);

        return mailSender;
    }
}
