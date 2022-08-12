package com.ioto.device.model;

import com.ioto.device.constants.DeviceType;
import com.ioto.device.model.custom.CustomDeviceType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Inventory {
    private String deviceId;
    private DeviceType type;
    private String customTypeName;
    private CustomDeviceType customType;
    private Map<String, Object> properties;
    private boolean allotted;
}
