package com.ioto.device.service;

import com.ioto.device.client.IotoAuthClient;
import com.ioto.device.model.DeviceAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthRefreshService {

    @Autowired
    private DeviceAccount deviceAccount;

    @Autowired
    private HttpHeaders httpHeaders;

    @Autowired
    private IotoAuthClient authClient;

    @Scheduled(cron = "0 */4 * ? * *") // run every 4 min
    public void refreshToken() {
        try {
            DeviceAccount response = authClient.refreshToken(deviceAccount.getRefresh_token());
            deviceAccount.setAccess_token(response.getAccess_token());
            deviceAccount.setRefresh_token(response.getRefresh_token());
            deviceAccount.setExpires_in(response.getExpires_in());

            String authHeader = deviceAccount.getToken_type() +" "+ deviceAccount.getAccess_token();
            httpHeaders.set("Authorization", authHeader);
        } catch (Exception ex) {
            log.error("Error while Sending Energy Measurement ", ex);
        }
    }
}
