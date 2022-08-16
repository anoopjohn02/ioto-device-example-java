package com.ioto.device.config.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

@Slf4j
@Profile("mqtt")
@Configuration
public class IotoMqttCallback implements MqttCallback {

    @Lazy
    @Autowired
    private MqttConfig mqttConfig;

    @Override
    public void connectionLost(Throwable throwable) {
        log.info("Mqtt Connection Lost...");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        log.warn("Received Unhandled message: " + s + " -> " + mqttMessage );
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.info("Delivery Completed...");
    }
}
