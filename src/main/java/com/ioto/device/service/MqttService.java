package com.ioto.device.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ioto.device.config.mqtt.MqttConfig;
import com.ioto.device.model.message.IotoMessage;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service("mqttService")
public class MqttService {

    @Lazy
    @Autowired
    private MqttConfig mqttConfig;

    private final String MESSAGE_URL = "app/message";

    private ObjectMapper mapper = new ObjectMapper();

    @Lazy
    @Autowired
    private IMqttClient mqttClient;

    @Scheduled(fixedDelay = 10000)
    public void execute() throws Exception{
        if(!mqttConfig.connected()){
            mqttConfig.connect();
        }
    }

    public void sendMessage(String deviceId, IotoMessage payload) throws Exception{
        String json = mapper.writeValueAsString(payload);
        String destination = MESSAGE_URL + "/" + deviceId;
        MqttMessage mqttMessage = new MqttMessage(json.getBytes());
        mqttMessage.setQos(0);
        mqttMessage.setRetained(true);

        mqttClient.publish(destination, mqttMessage);
    }
}
