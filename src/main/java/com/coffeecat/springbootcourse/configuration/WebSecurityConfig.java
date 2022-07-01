package com.coffeecat.springbootcourse.configuration;

import com.coffeecat.springbootcourse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration //Declare class as Bean used for configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //what URLs are freely accessible (matchUrls - /** match all urls and sub-urls)
        // : authorize all users to access all urls.
//        http.authorizeRequests().antMatchers("/**").permitAll();
        http.authorizeRequests()
                .antMatchers("/", "/about", "/register", "/registrationconfirmed","/invaliduser",
                        "/expiredtoken","/verifyemail","/confirmregister", "/search", "/profileimage/**").permitAll()
                .antMatchers("/js/*", "/css/*", "/img/**").permitAll() //any js,css & subdirs of img-directory
                .antMatchers("/addstatus", "/editstatus",
                        "deletestatus", "/viewstatus").hasRole("ADMIN") //allow only for users with ROLE_ADMIN!
                .antMatchers("/webjars/**", "/chat/**/**","/chatview/*","/profile","/profile/*", "/editprofile", "/upload-profile-photo",
                        "/save-interest", "/delete-interest").authenticated() //for all logged-in users!
                .anyRequest().denyAll() //Default,deny access to all other URLs! - Will redirect to login-screen
                .and() //new rule-section
                .formLogin().loginPage("/login").defaultSuccessUrl("/").permitAll() //what login-page, forward success where?
                .and()
                .logout().permitAll(); //Allow all users to access the Logout submit link
    }

    //new method for CSRF, hardcoded User values:
    @Autowired //find Spring Object with method
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password("{noop}1234") //specify no encoding!!
                .roles("USER");
    }


    //For UserService override method:
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }
}
