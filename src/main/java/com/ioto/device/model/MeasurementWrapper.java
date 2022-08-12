package com.ioto.device.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
public class MeasurementWrapper {
    Map<String, Measurement> measurements = new HashMap<>();

    public void update(String deviceId, Measurement measurement){
        measurements.put(deviceId, measurement);
    }

    public Optional<Measurement> get(String deviceId){
        if(measurements.containsKey(deviceId)){
            return Optional.of(measurements.get(deviceId));
        }
        return Optional.empty();
    }

    public List<Measurement> getAll(){
        return new ArrayList<>(measurements.values());
    }

    public void clear(){
        measurements.clear();
    }
}
