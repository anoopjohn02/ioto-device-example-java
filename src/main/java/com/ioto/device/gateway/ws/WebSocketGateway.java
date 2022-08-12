package com.ioto.device.gateway.ws;

import com.ioto.device.config.ws.HubStompSessionHandler;
import com.ioto.device.constants.MessageType;
import com.ioto.device.gateway.IotoGateway;
import com.ioto.device.model.DeviceAccount;
import com.ioto.device.model.message.Alert;
import com.ioto.device.model.message.DeviceStatus;
import com.ioto.device.model.message.IotoMessage;
import com.ioto.device.model.message.Operation;
import com.ioto.device.service.IotoMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Profile("websocket")
@Service("webSocketGateway")
public class WebSocketGateway implements IotoGateway {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketGateway.class);

    @Value("${ioto.hub.ws.url}")
    private String webSocketUrl;

    private StompSession iotoStompSession;

    @Autowired
    @Qualifier("iotoStompClient")
    private WebSocketStompClient iotoStompClient;

    @Autowired
    @Qualifier("iotoStompSessionHandler")
    private HubStompSessionHandler iotoStompSessionHandler;

    @Autowired
    private DeviceAccount deviceAccount;

    private final String msgUrl = "/app/message/";

    private Map<String, StompFrameHandler> subscriptions = new HashMap<>();

    @Override
    public void sendHeartBeat(String deviceId) throws Exception {
        String url = msgUrl + deviceId;
        if (!isConnected())
            connect(webSocketUrl);
        if (isConnected()) {
            logger.debug("Sending heartbeat {}", url);
            IotoMessage message = new IotoMessage();
            message.setDeviceId(deviceId);
            message.setMessageType(MessageType.HEART_BEAT);
            synchronized (iotoStompSession) {
                iotoStompSession.send(getStompHeaders(url), message);
            }
        } else {
            logger.debug("WS not connected");
        }
    }

    @Override
    public void sendStatus(String macAddress, DeviceStatus device) throws Exception {
        String url = msgUrl + macAddress;
        if (!isConnected())
            connect(webSocketUrl);
        if (isConnected()) {
            logger.debug("Sending status {}", url);
            IotoMessage message = new IotoMessage();
            message.setDeviceId(device.getDeviceId());
            message.setMessageType(MessageType.STATUS);
            message.setMessage(device);
            synchronized (iotoStompSession) {
                iotoStompSession.send(getStompHeaders(url), message);
            }
        } else {
            logger.debug("WS not connected");
        }
    }

    @Override
    public void sendOperation(String deviceId, Operation operation) throws Exception {
        String url = msgUrl + deviceId;
        IotoMessage message = new IotoMessage();
        message.setDeviceId(operation.getDeviceId());
        message.setMessageType(MessageType.OPERATION);
        message.setMessage(operation);
        if (isConnected()) {
            logger.debug("Sending operation {}", url);
            synchronized (iotoStompSession) {
                iotoStompSession.send(getStompHeaders(url), message);
            }
        } else {
            logger.warn("WS not connected");
        }
    }

    @Override
    public void sendAlert(String deviceId, Alert alert) throws Exception {

        String url = msgUrl + deviceId;
        IotoMessage message = new IotoMessage();
        message.setDeviceId(alert.getDeviceId());
        message.setMessageType(MessageType.ALERT);
        message.setMessage(alert);
        if (isConnected()) {
            logger.debug("Sending notification {}", url);
            synchronized (iotoStompSession) {
                iotoStompSession.send(getStompHeaders(url), message);
            }
        } else {
            logger.warn("WS not connected");
        }
    }

    @Override
    public void subscribe(String deviceId, IotoMessageHandler handler) throws Exception {
        StompFrameHandler stompFrameHandler = new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return IotoMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers,Object payload) {
                handler.onMessageReceived((IotoMessage) payload);
            }
        };

        String url = "/message/" + deviceId;
        if (isConnected()){
            iotoStompSession.subscribe(getStompHeaders(url), stompFrameHandler);
            logger.info("Subscribed {}", url);
        }
        subscriptions.put(url, stompFrameHandler);
    }

    @Override
    public boolean isConnected() {
        return iotoStompSession != null
                && iotoStompSession.isConnected();
    }

    private void connect(String webSocketUrl) throws Exception {
        logger.info("Connecting {}", webSocketUrl);

        WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
        webSocketHttpHeaders.add("X-AuthToken", deviceAccount.getAccess_token());

        ListenableFuture<StompSession> listenableFuture = iotoStompClient.connect(
                webSocketUrl, webSocketHttpHeaders, getStompHeaders(), iotoStompSessionHandler);
        iotoStompSession = listenableFuture.get();
        logger.info("WS connected");
        if (subscriptions.size() > 0) {
            subscriptions.keySet().forEach(s -> {
                logger.info("AUTO Subscribing url {}", s);
                iotoStompSession.subscribe(getStompHeaders(s), subscriptions.get(s));
                logger.info("Subscribed {}", s);
            });
        }
    }

    private StompHeaders getStompHeaders(String destination){
        StompHeaders stompHeaders = getStompHeaders();
        stompHeaders.setDestination(destination);
        return stompHeaders;
    }

    private StompHeaders getStompHeaders(){
        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.add("X-AuthToken", deviceAccount.getAccess_token());
        return stompHeaders;
    }

}
