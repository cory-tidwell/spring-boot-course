package com.coffeecat.springbootcourse.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    public static final String DESTINATION_PREFIX = "/topic";
    public static final String REGISTRY = "/chat";

    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        //served over URL "/chat", fallback to SockJS if unavailable.
        registry.addEndpoint(REGISTRY).setAllowedOrigins("*").withSockJS()
                .setClientLibraryUrl("/webjars/sockjs-client/sockjs.min.js"); //add client JS-Library.
    }

    @Override
    public void configureMessageBroker(final MessageBrokerRegistry registry) {
        //enables Spring in-memory message broker, queue for user2user communication.
        registry.enableSimpleBroker(DESTINATION_PREFIX);
        registry.setApplicationDestinationPrefixes("/app");
    }
}