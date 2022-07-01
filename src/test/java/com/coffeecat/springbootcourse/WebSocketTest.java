package com.coffeecat.springbootcourse;

import com.coffeecat.springbootcourse.configuration.WebSocketConfig;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WebSocketTest {

    private static WebClient firstClient;

    @BeforeAll
    public void setup() throws ExecutionException, InterruptedException, TimeoutException {

        RunStompFrameHandler runStopFrameHandler = new RunStompFrameHandler(new CompletableFuture<>());

        String wsUrl = "ws://127.0.0.1:8080" + WebSocketConfig.REGISTRY;
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSession stompSession = stompClient
                .connect(wsUrl, new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        firstClient = WebClient.builder()
                .stompClient(stompClient)
                .stompSession(stompSession)
                .handler(runStopFrameHandler)
                .build();
    }

    @AfterAll
    public void tearDown() {
        if(firstClient.getStompSession().isConnected()) {
            firstClient.getStompSession().disconnect();
            firstClient.getStompClient().stop();
        }
    }

    @Test
    public void startTest() {

    }

    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));

        return transports;
    }

    @Data
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private class RunStompFrameHandler implements StompFrameHandler {

        CompletableFuture<Object> future;
        @Override
        public Type getPayloadType(StompHeaders headers) {
            System.out.println(headers.toString());
            return byte[].class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            System.out.println(payload);
            future.complete(payload);
            future = new CompletableFuture<>();
        }
    }

    @Data
    @Builder
    @FieldDefaults(level =  AccessLevel.PRIVATE)
    private static class WebClient {
        WebSocketStompClient stompClient;
        StompSession stompSession;
        String sessionToken;
        RunStompFrameHandler handler;
    }
}
