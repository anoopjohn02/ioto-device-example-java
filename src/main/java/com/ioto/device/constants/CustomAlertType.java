package com.ioto.device.constants;

public enum CustomAlertType {
    HIGH_VOLTAGE("app.notification.device.voltage.high","Voltage High"),
    ENERGY_CONSUMPTION_CROSSED("app.notification.device.energy.usage.high","Energy Usage Crossed the limit");

    private final String key;
    private final String description;

    private CustomAlertType(String key, String description){

        this.key = key;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }
}
