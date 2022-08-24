package com.ioto.device.examples;

import com.ioto.device.constants.CustomAlertType;
import com.ioto.device.constants.Severity;
import com.ioto.device.model.CustomDevice;
import com.ioto.device.model.Measurement;
import com.ioto.device.model.message.Alert;
import com.ioto.device.service.AppNotificationService;
import com.ioto.device.service.MeasurementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class RandomMeasurementService {

    @Autowired
    @Qualifier("measurementService")
    private MeasurementService measurementService;

    @Autowired
    @Qualifier("appNotificationService")
    private AppNotificationService<CustomDevice> appNotificationService;

    @Value("${deviceId}")
    private String deviceId;

    @Autowired
    @Qualifier("device")
    private CustomDevice device;

    @Scheduled(cron = "0 0 0-23 ? * *") // run every hour
    public void sendEnergyMeasurement() {
        try {
            int energy = getRandomNumbers(500, 1500);
            Measurement measurement = new Measurement();
            measurement.setDeviceId(deviceId);
            measurement.setEnd(new Date());
            measurement.setStart(Date.from(getStartDate(60).toInstant()));
            measurement.setMetadata(new HashMap());
            measurement.setValue(energy);
            measurement.setUnit("Wh");
            measurement.setType("ENERGY_CONSUMPTION");
            measurementService.send(List.of(measurement));
            if (energy > 1200){
                sendAlert(CustomAlertType.ENERGY_CONSUMPTION_CROSSED);
            }
        } catch (Exception ex) {
            log.error("Error while Sending Energy Measurement ", ex);
        }
    }

    @Scheduled(cron = "0 0/30 * * * ?") // run every 30 min
    public void sendVoltageMeasurement() {
        try {
            int voltage = getRandomNumbers(100, 350);
            Measurement measurement = new Measurement();
            measurement.setDeviceId(deviceId);
            measurement.setEnd(new Date());
            measurement.setStart(Date.from(getStartDate(30).toInstant()));
            measurement.setMetadata(new HashMap());
            measurement.setValue(voltage);
            measurement.setUnit("V");
            measurement.setType("VOLTAGE_USAGE");
            measurementService.send(List.of(measurement));
            if (voltage > 250){
                sendAlert(CustomAlertType.HIGH_VOLTAGE);
            }
        } catch (Exception ex) {
            log.error("Error while Sending Voltage Measurement ", ex);
        }
    }

    private ZonedDateTime getStartDate(int minute) {
        ZonedDateTime now = LocalDateTime.now().atZone(ZoneId.systemDefault());
        if (now.getMinute() >= minute) {
            return now.withMinute(minute);
        } else {
            return now.withMinute(0);
        }
    }

    private int getRandomNumbers(int low, int high) {
        Random random = new Random();
        int result = random.nextInt(high - low) + low;
        return result;
    }

    private void sendAlert(CustomAlertType alertType) throws Exception{
        Alert alert = new Alert();
        alert.setDeviceId(deviceId);
        alert.setKey(alertType.getKey());
        alert.setDescription(alertType.getDescription());
        alert.setSeverity(Severity.MAJOR);
        alert.setType(alertType.name());
        alert.setDateTime(new Date());

        appNotificationService.sendAlert(alert, device);
    }
}
