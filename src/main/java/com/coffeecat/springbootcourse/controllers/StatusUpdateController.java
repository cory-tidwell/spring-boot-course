package com.coffeecat.springbootcourse.controllers;
import com.coffeecat.springbootcourse.model.entity.StatusUpdate;
import com.coffeecat.springbootcourse.service.StatusUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class StatusUpdateController {
    //wire in Service for StatusUpdate:
    @Autowired
    private StatusUpdateService statusUpdateService;

    @RequestMapping(value="/editstatus", method=RequestMethod.GET)
    ModelAndView editStatus(ModelAndView modelAndView, @RequestParam(name="id") Long id) {
        StatusUpdate statusUpdate = statusUpdateService.get(id);

        modelAndView.getModel().put("statusUpdate", statusUpdate);
        modelAndView.setViewName("app.editStatus");
        return modelAndView;
    }

    //Posting the edited Status Update:
    //check if Form is @Valid
    @RequestMapping(value="/editstatus", method=RequestMethod.POST)
    ModelAndView editStatus(ModelAndView modelAndView, @Valid StatusUpdate statusUpdate, BindingResult result) {
        modelAndView.setViewName("app.editStatus");

        if(!result.hasErrors()) {
            statusUpdateService.save(statusUpdate);
            modelAndView.setViewName("redirect:/viewstatus");
        }

        return modelAndView;
    }

    @RequestMapping(value="/deletestatus", method=RequestMethod.GET)
    ModelAndView deleteStatus(ModelAndView modelAndView, @RequestParam(name="id") Long id) {
        statusUpdateService.delete(id);
        modelAndView.setViewName("redirect:/viewstatus");
        return modelAndView;
    }

    @RequestMapping(value="/viewstatus", method= RequestMethod.GET)
    ModelAndView viewStatus(ModelAndView modelAndView, @RequestParam(name="p", defaultValue="1") int pageNumber) {
        Page<StatusUpdate> page = statusUpdateService.getPage(pageNumber);
        //pass page to model and view:
        modelAndView.getModel().put("page", page);
        modelAndView.setViewName("app.viewStatus");
        return modelAndView;
    }

    //add additional Method invoked to Mapping:
    @RequestMapping(value="/addstatus", method= RequestMethod.GET)
    //instead of just returning View(String), return ModelAndView Object. - Now can take Data too(via Form)!
    ModelAndView addStatus(ModelAndView modelAndView,
                           @ModelAttribute("statusUpdate") StatusUpdate statusUpdate) { //@(attribute from form-tag in jsp):create Obj, add to Model

        modelAndView.setViewName("app.addStatus");

        StatusUpdate latestStatusUpdate = statusUpdateService.getLatest();

        //Model is a Map:
        modelAndView.getModel().put("latestStatusUpdate", latestStatusUpdate);

        return modelAndView;
    }

    @RequestMapping(value="/addstatus", method= RequestMethod.POST) //submit form with POST
    ModelAndView addStatus(ModelAndView modelAndView, @Valid StatusUpdate statusUpdate, BindingResult result) {
        //@Valid annot:used to check if form valid to constraints defined in StatusUpdate class-> result returned to BindingResult
        modelAndView.setViewName("app.addStatus");

        if(!result.hasErrors()) {
            statusUpdateService.save(statusUpdate);
            //clear form after submit : load new StatusUpdate-Object into form.
            modelAndView.getModel().put("statusUpdate", new StatusUpdate());

            //redirect when update has been sent successfully:
            modelAndView.setViewName("redirect:/viewstatus");
        }

        StatusUpdate latestStatusUpdate = statusUpdateService.getLatest(); //for displaying input:
        modelAndView.getModel().put("latestStatusUpdate", latestStatusUpdate);

        return modelAndView;
    }
}
