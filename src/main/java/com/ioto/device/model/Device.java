package com.ioto.device.model;

import com.ioto.device.constants.DeviceType;
import com.ioto.device.constants.Status;
import com.ioto.device.model.message.Operation;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Device {
    private String macAddress;
    private Long userId;
    private Long companyId;
    private Status status;
    private String name;
    private String description;
    private DeviceType type;
    private Date lastAvailTime;
    private boolean disable;

    private boolean canRead;
    private boolean canCreate;
    private boolean canEdit;
    private boolean canDelete;

    private Inventory inventory;
    private Operation operationExecuted;
}
