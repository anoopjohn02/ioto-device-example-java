package com.ioto.device.service;

import com.ioto.device.client.IotoMeasurementClient;
import com.ioto.device.model.Measurement;
import com.ioto.device.model.MeasurementWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service("measurementService")
public class MeasurementService {

    private static final Logger logger = LoggerFactory.getLogger(MeasurementService.class);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    private MeasurementWrapper measurements;

    @Autowired
    @Qualifier("measurementClient")
    private IotoMeasurementClient measurementClient;

    public void updateMeasurement(String deviceId, Measurement measurement){
        ZonedDateTime startTime = getStartDate();
        measurement.setEnd(new Date());
        measurement.setStart(Date.from(startTime.toInstant()));
        String key = deviceId + "-" + formatter.format(startTime);
        measurements.update(key, measurement);
    }

    public void send(){
        logger.info("Sending all measurements");
        List<Measurement> measurementList = measurements.getAll();
        if(measurementList.size() > 0) {
            measurementClient.send(measurementList);
        } else {
            logger.info("---No measurement to send");
        }
        measurements.clear();
    }

    private ZonedDateTime getStartDate(){
        ZonedDateTime now = LocalDateTime.now().atZone(ZoneId.systemDefault());
        if(now.getMinute() >= 30){
            return now.withMinute(30);
        } else {
            return now.withMinute(0);
        }
    }
}
