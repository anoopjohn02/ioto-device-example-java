package com.ioto.device.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class IotoUser {

    private Long userId;
    private String userName;
    private String type;
    private Long companyId;
    private String token;
    private boolean companyUser;
    private final List<String> roles;
    private String manufacturerId;
}
