package com.ioto.device.gateway.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ioto.device.config.mqtt.IotoMqttListener;
import com.ioto.device.constants.MessageType;
import com.ioto.device.gateway.IotoGateway;
import com.ioto.device.model.DeviceAccount;
import com.ioto.device.model.message.Alert;
import com.ioto.device.model.message.DeviceStatus;
import com.ioto.device.model.message.IotoMessage;
import com.ioto.device.model.message.Operation;
import com.ioto.device.service.IotoMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Profile("mqtt")
@Service("mqttGateway")
public class MqttGateway implements IotoGateway {

    private final String MESSAGE_URL = "app/message";

    private ObjectMapper mapper = new ObjectMapper();

    @Lazy
    @Autowired
    private IMqttClient mqttClient;

    @Autowired
    private DeviceAccount deviceAccount;

    @Autowired
    private IotoMqttListener mqttListener;

    @Override
    public void sendHeartBeat(String deviceId) throws Exception {
        if (isConnected()) {
            log.debug("Sending heartbeat {}", deviceId);
            IotoMessage message = new IotoMessage();
            message.setDeviceId(deviceId);
            message.setMessageType(MessageType.HEART_BEAT);
            sendMessage(deviceId, message);
        } else {
            log.debug("MQTT not connected");
        }
    }

    @Override
    public void sendStatus(String deviceId, DeviceStatus device) throws Exception {
        if (isConnected()) {
            log.debug("Sending status {}", deviceId);
            IotoMessage message = new IotoMessage();
            message.setDeviceId(device.getDeviceId());
            message.setMessageType(MessageType.STATUS);
            message.setMessage(device);
            sendMessage(deviceId, message);
        } else {
            log.debug("MQTT not connected");
        }
    }

    @Override
    public void sendOperation(String deviceId, Operation operation) throws Exception {
        if (isConnected()) {
            log.debug("Sending operation {}", deviceId);
            IotoMessage message = new IotoMessage();
            message.setDeviceId(operation.getDeviceId());
            message.setMessageType(MessageType.OPERATION);
            message.setMessage(operation);
            sendMessage(deviceId, message);
        } else {
            log.debug("MQTT not connected");
        }
    }

    @Override
    public void sendAlert(String deviceId, Alert alert) throws Exception {
        if (isConnected()) {
            log.debug("Sending Alert {}", deviceId);
            IotoMessage message = new IotoMessage();
            message.setDeviceId(alert.getDeviceId());
            message.setMessageType(MessageType.ALERT);
            message.setMessage(alert);
            sendMessage(deviceId, message);
        } else {
            log.debug("MQTT not connected");
        }
    }

    @Override
    public void subscribe(String deviceId, IotoMessageHandler handler) throws Exception {
        mqttClient.subscribeWithResponse(MESSAGE_URL + "/" + deviceId, mqttListener);
        mqttListener.addToMessageHandler(deviceId, handler);
    }

    @Override
    public boolean isConnected() {
        return mqttClient != null && mqttClient.isConnected();
    }

    private void sendMessage(String deviceId, IotoMessage payload) throws Exception{
        String json = mapper.writeValueAsString(payload);
        String destination = MESSAGE_URL + "/" + deviceId;
        MqttMessage mqttMessage = new MqttMessage(json.getBytes());
        mqttMessage.setQos(0);
        mqttMessage.setRetained(true);
        mqttClient.publish(destination, mqttMessage);
    }
}
