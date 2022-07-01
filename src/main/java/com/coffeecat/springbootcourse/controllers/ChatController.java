package com.coffeecat.springbootcourse.controllers;

import com.coffeecat.springbootcourse.model.dto.SimpleMessage;
import com.coffeecat.springbootcourse.model.entity.SiteUser;
import com.coffeecat.springbootcourse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import java.security.Principal;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpleMsgTemplate;

    @Autowired
    private Util util;

    @Autowired
    private UserService userService;

    @RequestMapping("/chatview/{chatwithUID}")
    ModelAndView chat(ModelAndView modelAndView, @PathVariable Long chatwithUID) {
        SiteUser user = util.getUser();
        String chatwithUsername = userService.getUserName(chatwithUID);

        modelAndView.getModel().put("uid", user.getId());
        modelAndView.getModel().put("chatwithUID", chatwithUID);
        modelAndView.getModel().put("chatwithUsername", chatwithUsername);

        modelAndView.setViewName("chat.chatview");

        return modelAndView;
    }

    @MessageMapping("/message/send/{toUID}") //annotation for handling Messages
    public void send(Principal principal, SimpleMessage message, @DestinationVariable Long toUID) { //pathVar = destVar!!

        String fromUsername = principal.getName(); //use SpringPrincipal Object to get the Username
        SiteUser fromUser = userService.get(fromUsername);
        Long fromUID = fromUser.getId();

        System.out.println(StringUtils.repeat("-", 5) + "SIMPLE MESSAGE" + StringUtils.repeat("-", 5));
        System.out.println(message);
        System.out.println(StringUtils.repeat("-", 10));

        String returnReceiptQueue = String.format("/queue/%d", fromUID);
        simpleMsgTemplate.convertAndSendToUser(fromUsername, returnReceiptQueue, message);
    }
}
