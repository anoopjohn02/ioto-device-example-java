package com.ioto.device.client;

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

    @Value("${ioto.auth.url}")
    private String authUrl;

    @Value("${deviceId}")
    private String macAddress;

    @Value("${ioto.auth.password}")
    private String devicePassword;

    @Value("${ioto.client.id}")
    private String clientId;

    @Value("${ioto.client.password}")
    private String clientPassword;

    public DeviceAccount authenicate(){

        HttpHeaders headers = createHeaders();
        HttpEntity<List<Object>> request = new HttpEntity<>(headers);

        String url = authUrl + "/oauth/token?grant_type=password&username={device_uname}&password={device_pwd}";
        UriComponents uriComponents =
                UriComponentsBuilder.fromUriString(url)
                        .build().expand(macAddress, devicePassword).encode();
        LOGGER.debug("Accessing URL {}", uriComponents.toUriString());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<DeviceAccount> response = restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, request, DeviceAccount.class);
        return response.getBody();
    }

    private HttpHeaders createHeaders() {
        String auth = clientId + ":" + clientPassword;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")) );
        String authHeader = "Basic " + new String( encodedAuth );

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", authHeader);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        return httpHeaders;
    }
}
