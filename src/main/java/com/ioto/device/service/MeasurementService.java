package com.ioto.device.service;

import com.ioto.device.client.IotoMeasurementClient;
import com.ioto.device.model.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service("measurementService")
public class MeasurementService {

    private static final Logger logger = LoggerFactory.getLogger(MeasurementService.class);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Lazy
    @Autowired
    @Qualifier("measurementClient")
    private IotoMeasurementClient measurementClient;

    public void send(List<Measurement> measurementList){
        logger.info("Sending all measurements");
        if(measurementList.size() > 0) {
            measurementClient.send(measurementList);
        } else {
            logger.info("---No measurement to send");
        }
    }
}
