package com.ioto.device.model.custom;

import lombok.Data;

import java.util.Map;

@Data
public class CustomDeviceType {
    private String typeName;
    private Map<String, Object> properties;
    private Map<String, DeviceTypeProperty> invFields;
}
