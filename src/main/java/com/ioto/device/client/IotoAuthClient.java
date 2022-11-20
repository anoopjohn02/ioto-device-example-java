package com.ioto.device.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ioto.device.model.AuthForm;
import com.ioto.device.model.DeviceAccount;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.Charset;
import java.util.List;

@Service("authClient")
public class IotoAuthClient {

    private final Logger LOGGER = LoggerFactory.getLogger(IotoAuthClient.class);

    @Value("${ioto.login.url}")
    private String authUrl;

    @Value("${deviceId}")
    private String macAddress;

    @Value("${ioto.auth.password}")
    private String devicePassword;

    @Value("${ioto.client.id}")
    private String clientId;

    public DeviceAccount authenicate(){

        AuthForm authForm = new AuthForm();
        authForm.setGrant_type("password");
        authForm.setClient_id(clientId);
        authForm.setUsername(macAddress);
        authForm.setPassword(devicePassword);

        HttpHeaders headers = createHeaders();
        HttpEntity<AuthForm> request = new HttpEntity<>(authForm, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new ObjectToUrlEncodedConverter(new ObjectMapper()));
        ResponseEntity<DeviceAccount> response = restTemplate.exchange(authUrl, HttpMethod.POST, request, DeviceAccount.class);
        return response.getBody();
    }

    public DeviceAccount refreshToken(String refreshToken){

        LOGGER.info("Refreshing Token");
        AuthForm authForm = new AuthForm();
        authForm.setGrant_type("refresh_token");
        authForm.setClient_id(clientId);
        authForm.setRefresh_token(refreshToken);

        HttpHeaders headers = createHeaders();
        HttpEntity<AuthForm> request = new HttpEntity<>(authForm, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new ObjectToUrlEncodedConverter(new ObjectMapper()));
        ResponseEntity<DeviceAccount> response = restTemplate.exchange(authUrl, HttpMethod.POST, request, DeviceAccount.class);
        return response.getBody();
    }

    private HttpHeaders createHeaders() {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        return httpHeaders;
    }
}
