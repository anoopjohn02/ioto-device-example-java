package com.ioto.device.service;

import com.ioto.device.client.IotoHubRestClient;
import com.ioto.device.constants.Status;
import com.ioto.device.exceptions.DeviceRuntimeException;
import com.ioto.device.model.CustomerDevices;
import com.ioto.device.model.Device;
import com.ioto.device.model.message.Alert;
import com.ioto.device.model.message.DeviceStatus;
import com.ioto.device.model.message.Notification;
import com.ioto.device.model.message.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service("deviceService")
public class DeviceService<M extends Device> {

    private static final Logger logger = LoggerFactory.getLogger(DeviceService.class);

    @Autowired
    @Qualifier("gatewayService")
    private GatewayService gatewayService;

    @Value("${deviceId}")
    private String macAddress;

    @Autowired
    @Qualifier("hubRestClient")
    private IotoHubRestClient<M> restClient;


    public CustomerDevices getAllDevices(){
        return restClient.getAllDevices(macAddress);
    }

    public M getDevice(Class<M> clazz) throws DeviceRuntimeException {
        logger.debug("Fetching device {}", macAddress);
        if(StringUtils.isEmpty(macAddress)){
            throw new DeviceRuntimeException("macAddress is empty");
        }
        M device =  restClient.getDevice(macAddress, clazz);
        device.setStatus(Status.ONLINE);
        return device;
    }

    public boolean sendStatus(M device, Class<M> clazz, Object deviceData)throws Exception {
        if(isConnected()){
            logger.debug("Status sending.");
            gatewayService.sendStatus(getMessage(device, deviceData));
            return true;
        }
        return false;
    }

    private DeviceStatus getMessage(Device device, Object deviceData){
        DeviceStatus message = new DeviceStatus();
        message.setDeviceData(deviceData);
        message.setDeviceId(device.getMacAddress());
        message.setLastAvailTime(device.getLastAvailTime());
        message.setOperationExecuted(device.getOperationExecuted());
        message.setStatus(device.getStatus());
        message.setType(device.getType());
        return message;
    }

    public boolean subscribe(String deviceId, IotoMessageHandler handler)throws Exception {
        if(isConnected()){
            gatewayService.subscribe(deviceId, handler);
            return true;
        }
        return false;
    }

    public boolean sendOperation(String deviceId, Operation operation)throws Exception {
        if(isConnected()){
            gatewayService.sendOperation(deviceId, operation);
            return true;
        }
        return false;
    }

    public boolean sendAlert(String deviceId, Alert alert)throws Exception {
        if(isConnected()){
            gatewayService.sendAlert(deviceId, alert);
            return true;
        }
        return false;
    }

    public boolean sendNotification(String deviceId, Notification notification)throws Exception {
        if(isConnected()){
            gatewayService.sendNotification(deviceId, notification);
            return true;
        }
        return false;
    }

    public boolean isConnected() throws DeviceRuntimeException{
        return gatewayService.isConnected();
    }
}
