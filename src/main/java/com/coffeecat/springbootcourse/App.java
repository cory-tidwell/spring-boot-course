package com.coffeecat.springbootcourse;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;

//@Controller //(Model-View-Controller - decide what content is served to browser) -> now separate

//replace:
    //@EnableAutoConfiguration
    //@ComponentScan //@START-UP: search for other Applications marked with annotations
//with:
@EnableAsync //allows Methods to run independently!
@SpringBootApplication
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true) //extra security, prepost-disables Users from running Methods unless they have permission
//Added extends for Spring-Boot-Starter-Tomcat dependency configuration: !!
public class App extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args); //(what to run, String arguments from command-line)
    }

    //Spring-Boot-Starter-Tomcat config:
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(App.class);
    }

    @Bean
    public TilesConfigurer tilesConfigurer() {
        TilesConfigurer tilesConfigurer = new TilesConfigurer();
        //what configuration is used:
        String[] defs = {"/WEB-INF/tiles.xml"};
        tilesConfigurer.setDefinitions(defs);
        return tilesConfigurer;
    }

    //flag method as bean!
    @Bean
    public UrlBasedViewResolver tilesViewResolver() {
        UrlBasedViewResolver tilesViewResolver = new UrlBasedViewResolver();
        tilesViewResolver.setViewClass(TilesView.class);
        return tilesViewResolver;
    }

    //For Password Encryption ( App.java loaded early, only one instance can be used across multiple classes. ):
    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }


    //Handling 403:Errors
    @Configuration
    public class ServerConfig {
        @Bean
        public ConfigurableServletWebServerFactory webServerFactory() {
            TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();

            //redirects Requests to custom 403-Page:
            factory.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/403"));
            return factory;
        }
    }

    //HTML Sanitizer:
    @Bean
    public PolicyFactory getUserHtmlPolicy() {
        return new HtmlPolicyBuilder()
                .allowCommonBlockElements()
                .allowCommonInlineFormattingElements()
                .toFactory();
    }
}
