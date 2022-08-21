package com.ioto.device.service;

import com.ioto.device.model.Device;
import com.ioto.device.model.message.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("appNotificationService")
public class AppNotificationService<M extends Device>  {

    @Autowired
    @Qualifier("deviceService")
    private DeviceService<M> deviceService;

    private static final Logger logger = LoggerFactory.getLogger(AppNotificationService.class);

    public void sendAlert(Alert alert, Device device) throws Exception {
        logger.info("Sending notification {} to {}", alert.getKey(), device.getMacAddress());
        deviceService.sendAlert(device.getMacAddress(), alert);
    }
}
