package com.ioto.device.model;

import com.ioto.device.model.message.Alert;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class NotificationWrapper {

    private Map<String, LocalDateTime> timeMap = new HashMap<>();

    public boolean isReadyToSent(Alert alert){
        String key = getKey(alert);
        if(timeMap.containsKey(key)){
            LocalDateTime lastSentTime = timeMap.get(key);
            if(getTimeDifference(LocalDateTime.now(), lastSentTime) > 5){
                timeMap.put(key, LocalDateTime.now());
                return true;
            }
            return false;
        } else {
            timeMap.put(key, LocalDateTime.now());
            return true;
        }
    }

    private String getKey(Alert alert){
        return alert.getDeviceId() + alert.getKey();
    }

    private long getTimeDifference(LocalDateTime dateTimeNow, LocalDateTime dateTime) {
        LocalDateTime tempDateTime = LocalDateTime.from(dateTime);
        return tempDateTime.until(dateTimeNow, ChronoUnit.HOURS);
    }

}
