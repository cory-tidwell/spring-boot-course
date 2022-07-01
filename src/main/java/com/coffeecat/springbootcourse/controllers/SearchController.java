package com.coffeecat.springbootcourse.controllers;

import com.coffeecat.springbootcourse.model.dto.SearchResult;
import com.coffeecat.springbootcourse.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    //Mapping can handle both POST and GET methods!
    @RequestMapping(value="/search", method={RequestMethod.POST, RequestMethod.GET})
    public ModelAndView search(ModelAndView modelAndView, @RequestParam("profilesearch") String text,
                               @RequestParam(name="p", defaultValue="1") int pageNumber) {

        //collect SearchResult Objects:
        Page<SearchResult> searchResultList = searchService.search(text,pageNumber);

        modelAndView.getModel().put("profilesearch", text);
        modelAndView.getModel().put("page", searchResultList);
        modelAndView.setViewName("app.search");

        return modelAndView;
    }
}
