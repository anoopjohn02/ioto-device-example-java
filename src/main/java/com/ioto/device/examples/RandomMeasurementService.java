package com.ioto.device.examples;

import com.ioto.device.model.Measurement;
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

    @Value("${deviceId}")
    private String deviceId;

    @Scheduled(cron = "0 0 0-23 ? * *") // run every hour
    public void sendEnergyMeasurement() {
        try {
            int energy = getRandomNumbers(500, 1000);
            Measurement measurement = new Measurement();
            measurement.setDeviceId(deviceId);
            measurement.setEnd(new Date());
            measurement.setStart(Date.from(getStartDate(60).toInstant()));
            measurement.setMetadata(new HashMap());
            measurement.setValue(energy);
            measurement.setUnit("Wh");
            measurement.setType("ENERGY_CONSUMPTION");
            measurementService.send(List.of(measurement));

        } catch (Exception ex) {
            log.error("Error while Sending Energy Measurement ", ex);
        }
    }

    @Scheduled(cron = "0 0/30 * * * ?") // run every 30 min
    public void sendVoltageMeasurement() {
        try {
            int voltage = getRandomNumbers(150, 250);
            Measurement measurement = new Measurement();
            measurement.setDeviceId(deviceId);
            measurement.setEnd(new Date());
            measurement.setStart(Date.from(getStartDate(30).toInstant()));
            measurement.setMetadata(new HashMap());
            measurement.setValue(voltage);
            measurement.setUnit("Wh");
            measurement.setType("VOLTAGE_USAGE");
            measurementService.send(List.of(measurement));
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
}
