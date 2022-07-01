package com.coffeecat.springbootcourse.controllers;

import com.coffeecat.springbootcourse.model.entity.StatusUpdate;
import com.coffeecat.springbootcourse.service.StatusUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

//!!Controller annotation has to be added!! -> creates Object from this class (bean)
@Controller

public class PageController {

    //403Error-Message
    @Value("${message.error.forbidden}")
    private String accessDeniedMessage;

    @Autowired //find StatusUpdate Obj automatically when creating.
    private StatusUpdateService statusUpdateService;

    @RequestMapping("/") //what URL is this the response to: (Request is handled by home-method)
//    @ResponseBody //specifies: Return value = return to browser
    ModelAndView home(ModelAndView modelAndView) {

        //get latest-StatusUpdates to the page.
        StatusUpdate statusUpdate = statusUpdateService.getLatest();
        modelAndView.getModel().put("statusUpdate", statusUpdate);

        //display statusUpdates automatically:
        modelAndView.setViewName("app.homepage");
        return modelAndView;
    }

    //Mapping Multiple:
    @RequestMapping("/about")
    String about() {
        return "app.about";
    }

    //Mapping Errors-Page:
    @RequestMapping("/403")
    ModelAndView accessDenied(ModelAndView modelAndView) {
        modelAndView.getModel().put("message", accessDeniedMessage);
        modelAndView.setViewName("app.message");
        return modelAndView;
    }
}
