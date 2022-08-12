package com.ioto.device.examples;

import com.ioto.device.constants.OperationStatus;
import com.ioto.device.model.CustomDevice;
import com.ioto.device.model.message.IotoMessage;
import com.ioto.device.model.message.Operation;
import com.ioto.device.service.DeviceService;
import com.ioto.device.service.IotoMessageHandler;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class OperationExecutingService implements IotoMessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(OperationExecutingService.class);

    @Autowired
    @Qualifier("deviceService")
    private DeviceService<CustomDevice> deviceService;

    @Autowired
    @Qualifier("device")
    private CustomDevice coordinator;

    @Value("${deviceId}")
    private String macAddress;

    @Autowired
    private ModelMapper modelMapper;

    @PostConstruct
    public void ready()throws Exception{
        subscribe();
    }

    private void subscribe()throws Exception{
        logger.info("Subscribing Operation Socket...");
        deviceService.subscribe(this.macAddress, this);
    }

    private void checkMessage(IotoMessage message){
        switch (message.getMessageType()){
            case OPERATION:
                Operation operation  = modelMapper.map(message.getMessage(), Operation.class);
                executeOperation(operation);
                break;
        }
    }

    private void executeOperation(Operation operation){
        try{
            operation.setStatus(OperationStatus.SUCCESS);
            operation.setFailureReason("SUCCESS");
            coordinator.setOperationExecuted(operation);
            deviceService.sendStatus(coordinator, CustomDevice.class, null);
        } catch (Exception ex) {
            logger.error("Unable to execute operation ", ex);
            operation.setStatus(OperationStatus.FAILED);
            operation.setFailureReason(ex.getMessage());
        }
    }

    @Override
    public void onMessageReceived(IotoMessage message) {
        checkMessage(message);
    }
}
