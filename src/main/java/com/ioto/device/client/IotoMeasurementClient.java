package com.ioto.device.client;

import com.ioto.device.model.Measurement;
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

import java.util.Arrays;
import java.util.List;

@Service("measurementClient")
public class IotoMeasurementClient {

    private final Logger LOGGER = LoggerFactory.getLogger(IotoMeasurementClient.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${ioto.hub.ms.url}")
    private String baseUrl;

    @Autowired
    private HttpHeaders httpHeaders;

    public List<Measurement> send(List<Measurement> measurements){

        HttpEntity<List<Measurement>> request = new HttpEntity<>(measurements, httpHeaders);

        String url = baseUrl + "/measurements";
        UriComponents uriComponents =
                UriComponentsBuilder.fromUriString(url)
                        .build().encode();
        LOGGER.debug("Accessing URL {}", uriComponents.toUriString());

        ResponseEntity<Measurement[]> response = restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, request, Measurement[].class);
        return Arrays.asList(response.getBody());
    }

}
