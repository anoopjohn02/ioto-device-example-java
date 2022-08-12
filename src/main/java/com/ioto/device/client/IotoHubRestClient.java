package com.ioto.device.client;

import com.ioto.device.model.CustomerDevices;
import com.ioto.device.model.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service("hubRestClient")
public class IotoHubRestClient<M extends Device> {
    private final Logger LOGGER = LoggerFactory.getLogger(IotoHubRestClient.class);

    @Value("${ioto.hub.ms.url}")
    private String baseUrl;

    @Value("${deviceType}")
    private String deviceType;

    @Autowired
    private HttpHeaders httpHeaders;

    @Autowired
    private RestTemplate restTemplate;

    public CustomerDevices getAllDevices(String macAddress){
        HttpEntity<Object> request = new HttpEntity<>(httpHeaders);

        String url = baseUrl + "/remote/{macAddress}/all";
        UriComponents uriComponents =
                UriComponentsBuilder.fromUriString(url)
                        .build().expand(macAddress).encode();
        LOGGER.debug("Accessing URL {}", uriComponents.toUriString());

        ResponseEntity<CustomerDevices> response = restTemplate
                .exchange(uriComponents.toUri(), HttpMethod.GET, request, CustomerDevices.class);
        return response.getBody();
    }

    public M getDevice(String macAddress, Class<M> clazz) {

        HttpEntity<Object> request = new HttpEntity<>(httpHeaders);

        String url = baseUrl + "/remote/{macAddress}/{deviceType}";
        UriComponents uriComponents =
                UriComponentsBuilder.fromUriString(url)
                        .build().expand(macAddress, deviceType).encode();
        LOGGER.debug("Accessing URL {}", uriComponents.toUriString());

        ResponseEntity<M> response = restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, request, clazz);
        return response.getBody();
    }

    public M updateDevice(String macAddress, M device, Class<M> clazz){

        HttpEntity<Object> request = new HttpEntity<>(device, httpHeaders);

        String url = baseUrl + "/remote/{macAddress}/{deviceType}";
        UriComponents uriComponents =
                UriComponentsBuilder.fromUriString(url)
                        .build().expand(macAddress, deviceType).encode();
        LOGGER.debug("Accessing URL {}", uriComponents.toUriString());

        ResponseEntity<M> response = restTemplate.exchange(uriComponents.toUri(), HttpMethod.PUT, request, clazz);
        return response.getBody();
    }

    public Device updateEdgeDevice(String deviceType, String macAddress, Object device){

        HttpEntity<Object> request = new HttpEntity<>(device, httpHeaders);

        String url = baseUrl + "/remote/{macAddress}/{deviceType}";
        UriComponents uriComponents =
                UriComponentsBuilder.fromUriString(url)
                        .build().expand(macAddress, deviceType).encode();
        LOGGER.debug("Accessing URL {}", uriComponents.toUriString());

        ResponseEntity<Device> response = restTemplate.exchange(uriComponents.toUri(), HttpMethod.PUT, request, Device.class);
        return response.getBody();
    }

}
