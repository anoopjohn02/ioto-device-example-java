package com.ioto.device.config.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Profile("mqtt")
@Configuration
public class MqttConfig {

    private static final String MQTT_PUBLISHER_ID = "ioto-device-";

    @Value("${ioto.mqtt.server.url}")
    private String host;

    @Value("${deviceId}")
    private String userName;

    @Value("${ioto.auth.password}")
    private String password;

    @Autowired
    private IotoMqttListener listener;

    @Autowired
    private IotoMqttCallback callback;

    private IMqttClient instance;

    @Bean
    public IMqttClient mqttClient() throws Exception {
        instance = new MqttClient(host, MQTT_PUBLISHER_ID + userName);
        return instance;
    }

    public void connect() throws Exception{
        log.info("Connecting Mqtt");
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setUserName(userName);
        options.setPassword(password.toCharArray());
        options.setConnectionTimeout(10);
        options.setAutomaticReconnect(true);
        //options.setSocketFactory(SSLSocketFactory.getDefault());
        instance.connect(options);
        instance.setCallback(callback);
        log.info("Mqtt connected");
    }

    public boolean connected(){
        return instance.isConnected();
    }
}
