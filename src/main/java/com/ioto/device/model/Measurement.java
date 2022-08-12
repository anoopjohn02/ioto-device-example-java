package com.ioto.device.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
public class Measurement {
    private String deviceId;
    private String switchId;
    private String type;
    private long value;
    private String unit;
    private Date start;
    private Date end;
    private long timeDiffInMillis;
    private Map<String, Object> metadata;
}
