package com.ioto.device.model.custom;

import com.ioto.device.constants.custom.PropertyType;
import lombok.Data;

import java.util.List;

@Data
public class DeviceTypeProperty {
    private PropertyType type;
    private String displayText;
    private boolean mandatory;
    private List<String> listValues;
}
