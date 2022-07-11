package com.coffeecat.springbootcourse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Service
public class MailService {

    //Using Thymeleaf as Template-Engine:
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender mailSender;

    //DEBUG - Disable sending for Testing
    @Value("${mail.enable}")
    private boolean enable;

    //from application.properties
    @Value("${site.url}")
    private String url;


    private void send(MimeMessagePreparator preparator) {
        if(enable) {
            mailSender.send(preparator); //if sending enabled send the prepared Message!
        }
    }

    //Thymeleaf - Configure the template Engine in MailService Constructor
    @Autowired
    public MailService( TemplateEngine templateEngine) {

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

        //look for mail-temple in resources.mail-templates subdirectory:
        templateResolver.setPrefix("mail-templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5"); //What type should engine resolve
        templateResolver.setCacheable(false); //don't cache the Emails!

        templateEngine.setTemplateResolver(templateResolver); //pass Resolver to Engine.

        this.templateEngine = templateEngine;
    }

    @Async //is now running independently of other methods (Asynchronous)
    public void sendVerificationEmail(String emailAddress, String token) {

        //For HTML-Format - replace with Thymeleaf Template!:
//        StringBuilder sb = new StringBuilder();
//        sb.append("<HTML");
//        sb.append("<p> Hello there, this is <strong>HTML</strong></p>");
//        sb.append("</HTML");

        Context context = new Context();
        context.setVariable("token", token);
        context.setVariable("url", url);

        // process(NameOfTemplate, Context - Variables)
        String emailContents = templateEngine.process("verifyemail", context);

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);

                message.setTo(emailAddress);
                message.setFrom(new InternetAddress("no-reply@codeEncounter.com")); //make random address name
                message.setSubject("Please verify your Email Address");
                message.setSentDate(new Date()); //set to now

                message.setText(emailContents, true); //supply HTML to Message.
            }
        };

        send(preparator);

    }
}
