package com.ioto.device.service;

import com.ioto.device.gateway.IotoGateway;
import com.ioto.device.model.message.Alert;
import com.ioto.device.model.message.DeviceStatus;
import com.ioto.device.model.message.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("gatewayService")
public class GatewayService {

    private static final Logger logger = LoggerFactory.getLogger(GatewayService.class);

    @Autowired
    private IotoGateway gateway;

    @Value("${deviceId}")
    private String macAddress;

    public void sendStatus(DeviceStatus device)throws Exception {
        gateway.sendStatus(macAddress, device);
    }

    public void sendEdgeStatus(String deviceId, DeviceStatus device)throws Exception {
        gateway.sendStatus(deviceId, device);
    }

    public void sendOperation(String deviceId, Operation operation)throws Exception {
        gateway.sendOperation(deviceId, operation);
    }

    public void sendAlert(String deviceId, Alert alert)throws Exception {
        gateway.sendAlert(deviceId, alert);
    }

    public void subscribe(String macAddress, IotoMessageHandler handler)throws Exception {
        gateway.subscribe(macAddress, handler);
    }

    public boolean isConnected(){
        return gateway.isConnected();
    }
}
