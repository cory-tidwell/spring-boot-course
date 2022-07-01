package com.coffeecat.springbootcourse.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice //Advice - can run in response to other things
public class GlobalExceptionHandler {

    //Error-Message:
    @Value("${message.error.exception}")
    private String exceptionMessage;

    @Value("${message.error.duplicate.user}")
    private String duplicateUserMessage;

    @ExceptionHandler(value= MultipartException.class)
    @ResponseBody //Instead of a view, return text.
    public String fileUploadHandler(Exception e) {
        e.printStackTrace();
        return "Error occurred uploading file.";
    }

    //Handling Method:
    @ExceptionHandler(value=Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.getModel().put("message", exceptionMessage);
        modelAndView.getModel().put("url", request.getRequestURL());
        modelAndView.getModel().put("exception", e);

        modelAndView.setViewName("app.exception");

        return modelAndView;
    }

    //Handling DataIntegrityViolationException:
    @ExceptionHandler(value= DataIntegrityViolationException.class)
    public ModelAndView duplicateUserHandler(HttpServletRequest request, Exception e) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.getModel().put("message", duplicateUserMessage);
        modelAndView.getModel().put("url", request.getRequestURL());
        modelAndView.getModel().put("exception", e);

        modelAndView.setViewName("app.exception");

        return modelAndView;
    }
}
