package com.ioto.device.examples;

import com.ioto.device.service.MeasurementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RandomMeasurementService {

    @Autowired
    @Qualifier("measurementService")
    private MeasurementService measurementService;


    @Scheduled(cron = "0 0 0-23 ? * *") // run every hour
    public void sendEnergyMeasurement(){
        try{

        } catch (Exception ex){
            log.error("Error while Sending Energy Measurement ", ex);
        }
    }

    @Scheduled(cron = "0 0/30 * * * ?") // run every 30 min
    public void sendVoltageMeasurement(){
        try{

        } catch (Exception ex){
            log.error("Error while Sending Voltage Measurement ", ex);
        }
    }
}
