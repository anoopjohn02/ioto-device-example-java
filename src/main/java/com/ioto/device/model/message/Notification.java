package com.ioto.device.model.message;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class Notification {
    private String uId;
    private String id;
    private String deviceId;
    private String type;
    private String key;
    private String description;
    private Date dateTime;
    private Map<String, Object> metadata;
}
