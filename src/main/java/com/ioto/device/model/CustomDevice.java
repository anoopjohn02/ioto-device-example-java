package com.ioto.device.model;

import lombok.Data;

import java.util.Map;

@Data
public class CustomDevice extends Device {
    private Map<String, Object> metadata;
}
