package com.ioto.device.model.message;

import com.ioto.device.constants.OperationStatus;
import com.ioto.device.constants.OperationType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Operation{
    private String deviceId;
    private String switchId;
    private OperationType type;
    private OperationStatus status;
    private String failureReason;
    private Map<String, Object> metadata;

}
