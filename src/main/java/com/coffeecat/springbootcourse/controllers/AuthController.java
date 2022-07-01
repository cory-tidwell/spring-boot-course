package com.coffeecat.springbootcourse.controllers;

import com.coffeecat.springbootcourse.model.entity.SiteUser;
import com.coffeecat.springbootcourse.model.entity.VerificationToken;
import com.coffeecat.springbootcourse.service.MailService;
import com.coffeecat.springbootcourse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Date;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    //From MailService - Verification Email:
    @Autowired
    private MailService emailService;

    @Value("${message.registration.confirmed}")
    private String registrationConfirmedMessage;

    @Value("${message.invalid.user}")
    private String invalidUserMessage;

    @Value("${message.expired.token}")
    private String expiredTokenMessage;

    @RequestMapping("/login")
    String admin() {
        return "app.login";
    }

    @RequestMapping("/verifyemail")
    String verifyEmail() {
        return "app.verifyemail";
    }

    //Invoked when confirming the Token-Email:
    @RequestMapping("/confirmregister")
    ModelAndView registrationConfirmed(ModelAndView modelAndView, @RequestParam("t") String tokenString) {

        //check token validity:
        VerificationToken token = userService.getVerificationToken(tokenString); //lookup token from DB

        if(token == null) {
            modelAndView.setViewName("redirect:/invaliduser");
            return modelAndView;
        }

        userService.deleteToken(token); //delete Token from DB if it exists and is used.

        Date expiryDate = token.getExpiry();
        if(expiryDate.before(new Date())) { //compare expiry Date of Token with current Date
            modelAndView.setViewName("redirect:/expiredtoken");
            return modelAndView;
        }
        SiteUser user = token.getUser();
        if(user == null) {
            modelAndView.setViewName("redirect:/invaliduser");
            return modelAndView;
        }
        //if all checks have been passed -> Enable User!
        user.setEnabled(true);
        userService.save(user);

        modelAndView.getModel().put("message", registrationConfirmedMessage);
        modelAndView.setViewName("app.message");
        return modelAndView;
    }

    @RequestMapping("/invaliduser")
    ModelAndView invalidUser(ModelAndView modelAndView) {
        modelAndView.getModel().put("message", invalidUserMessage);
        modelAndView.setViewName("app.message");
        return modelAndView;
    }
    @RequestMapping("/expiredtoken")
    ModelAndView expiredToken(ModelAndView modelAndView) {
        modelAndView.getModel().put("message", expiredTokenMessage);
        modelAndView.setViewName("app.message");
        return modelAndView;
    }


    //For displaying Register Page to User:
    @RequestMapping(value="/register", method=RequestMethod.GET)
    ModelAndView register (ModelAndView modelAndView) {

        SiteUser user = new SiteUser();

        modelAndView.getModel().put("user", user);
        modelAndView.setViewName("app.register");

        return modelAndView;
    }

    //For Submitting the Form:
    @RequestMapping(value="/register", method=RequestMethod.POST)
    ModelAndView register (ModelAndView modelAndView, @ModelAttribute(value="user") @Valid SiteUser user,
                           BindingResult result) {

        modelAndView.setViewName("app.register");

        if(!result.hasErrors()) { //form = valid
            userService.register(user); //register the User

            //generate Verification Token
            String token = userService.createVerificationToken(user);

            emailService.sendVerificationEmail(user.getEmail(), token);//send Verification email through MailService
            modelAndView.setViewName("redirect:/verifyemail");//redirect
        }

        return modelAndView;
    }
}
