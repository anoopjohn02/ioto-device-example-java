package com.ioto.device.examples;

import com.ioto.device.constants.CustomNotificationType;
import com.ioto.device.model.CustomDevice;
import com.ioto.device.model.message.Notification;
import com.ioto.device.service.AppNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;

@Slf4j
@Service
public class RandomNotificationsService {

    @Autowired
    @Qualifier("appNotificationService")
    private AppNotificationService<CustomDevice> appNotificationService;

    @Value("${deviceId}")
    private String deviceId;

    @Autowired
    @Qualifier("device")
    private CustomDevice device;

    @PostConstruct
    public void sendUpNotifications(){
        try{
            log.info("Up notification {}", device.getMacAddress());
            CustomNotificationType notificationType = CustomNotificationType.DEVICE_UP;
            Notification notification = new Notification();
            notification.setDeviceId(deviceId);
            notification.setKey(notificationType.getKey());
            notification.setDescription(notificationType.getDescription());
            notification.setType(notificationType.name());
            notification.setDateTime(new Date());

            appNotificationService.sendNotifications(notification, device);
        } catch (Exception ex) {
            log.error("Error while Sending up notifications ", ex);
        }
    }
}
