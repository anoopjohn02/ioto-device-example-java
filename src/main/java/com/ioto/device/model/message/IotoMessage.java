package com.ioto.device.model.message;

import com.ioto.device.constants.MessageType;
import lombok.Data;

@Data
public class IotoMessage {
    private boolean serverNotification;
    private MessageType messageType;
    private String deviceId;
    private Object message;
    private String token;
}
