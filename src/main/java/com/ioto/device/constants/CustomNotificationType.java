package com.ioto.device.constants;

public enum CustomNotificationType {
    DEVICE_UP("app.notification.device.voltage.high","Device Online");

    private final String key;
    private final String description;

    private CustomNotificationType(String key, String description){

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
