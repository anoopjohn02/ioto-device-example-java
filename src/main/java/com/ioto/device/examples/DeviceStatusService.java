package com.ioto.device.examples;

import com.ioto.device.constants.Status;
import com.ioto.device.model.CustomDevice;
import com.ioto.device.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DeviceStatusService {

    private static final Logger logger = LoggerFactory.getLogger(DeviceStatusService.class);

    @Autowired
    @Qualifier("deviceService")
    private DeviceService<CustomDevice> deviceService;

    @Value("${deviceId}")
    private String macAddress;

    @Autowired
    @Qualifier("device")
    private CustomDevice device;

    @Scheduled(fixedDelay = 5000)
    public void execute(){
        try{
            logger.debug("Sending status to hub");
            sendStatus();
            logger.debug("Status Sent");
        } catch (Exception ex){
            logger.error("Error occurred while updating device ", ex);
        }
    }

    public void sendStatus() throws Exception{
        device.setStatus(Status.ONLINE);
        device.setLastAvailTime(new Date());
        device.setOperationExecuted(null);
        deviceService.sendStatus(device, CustomDevice.class, null);
    }
}
