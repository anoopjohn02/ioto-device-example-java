package com.ioto.device.examples;

import com.ioto.device.constants.CustomAlertType;
import com.ioto.device.constants.Severity;
import com.ioto.device.model.CustomDevice;
import com.ioto.device.model.Measurement;
import com.ioto.device.model.message.Alert;
import com.ioto.device.service.AppNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class RandomAlertService {
    @Autowired
    @Qualifier("appNotificationService")
    private AppNotificationService<CustomDevice> appNotificationService;

    @Value("${deviceId}")
    private String deviceId;

    @Autowired
    @Qualifier("device")
    private CustomDevice device;

    @Scheduled(cron = "0 0 */4 * * ?") // run every 4 hours
    public void sendRandomAlert() {
        try {
            CustomAlertType alertType = CustomAlertType.DEVICE_OVER_USED;

            Alert alert = new Alert();
            alert.setDeviceId(deviceId);
            alert.setKey(alertType.getKey());
            alert.setDescription(alertType.getDescription());
            alert.setSeverity(Severity.MAJOR);
            alert.setType(alertType.name());
            alert.setDateTime(new Date());

            appNotificationService.sendAlert(alert, device);
        } catch (Exception ex) {
            log.error("Error while Sending Energy Measurement ", ex);
        }
    }
}
