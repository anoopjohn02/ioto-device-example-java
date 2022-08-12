package com.ioto.device.model.message;

import com.ioto.device.constants.DeviceType;
import com.ioto.device.constants.Status;
import lombok.Data;

import java.util.Date;

@Data
public class DeviceStatus{
    private String deviceId;
    private String name;
    private Status status;
    private Date lastAvailTime;
    private Object deviceData;
    private DeviceType type;
    private Operation operationExecuted;
}
