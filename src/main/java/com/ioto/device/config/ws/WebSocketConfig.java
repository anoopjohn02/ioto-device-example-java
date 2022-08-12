package com.ioto.device.config.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@Profile("websocket")
@Configuration
public class WebSocketConfig {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    @Value("${ioto.hub.ws.url}")
    private String webSocketUrl;

    @Bean("iotoStompClient")
    public WebSocketStompClient createStompClient()throws Exception {

        WebSocketClient simpleWebSocketClient = new StandardWebSocketClient();

        WebSocketStompClient stompClient = new WebSocketStompClient(simpleWebSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        return stompClient;
    }

    @Bean("iotoStompSessionHandler")
    public HubStompSessionHandler createHubStompSessionHandler(){
        return new HubStompSessionHandler();
    }
}
