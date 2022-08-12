package com.ioto.device.gateway;

import com.ioto.device.model.message.Alert;
import com.ioto.device.model.message.DeviceStatus;
import com.ioto.device.model.message.Operation;
import com.ioto.device.service.IotoMessageHandler;

public interface IotoGateway {

    void sendHeartBeat(String deviceId)throws Exception ;
    void sendStatus(String deviceId, DeviceStatus device)throws Exception ;
    void sendOperation(String deviceId, Operation operation)throws Exception;
    void sendAlert(String deviceId, Alert alert)throws Exception;

    void subscribe(String deviceId, IotoMessageHandler handler)throws Exception;
    boolean isConnected();

}
