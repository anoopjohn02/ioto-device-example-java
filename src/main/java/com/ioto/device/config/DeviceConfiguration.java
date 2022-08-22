package com.ioto.device.config;

import com.ioto.device.client.IotoAuthClient;
import com.ioto.device.exceptions.DeviceRuntimeException;
import com.ioto.device.model.CustomDevice;
import com.ioto.device.model.DeviceAccount;
import com.ioto.device.service.DeviceService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
public class DeviceConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DeviceConfiguration.class);

    @Autowired
    private IotoAuthClient authClient;

    @Autowired
    @Qualifier("deviceService")
    private DeviceService<CustomDevice> deviceService;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper;
    }

    @Bean
    public DeviceAccount authenticate(){
        logger.info("Authenticating device...");
        DeviceAccount account = authClient.authenicate();
        logger.info("Device authenticated...");
        return account;
    }

    @Bean
    @Primary
    public HttpHeaders createHeaders() throws Exception{

        DeviceAccount account = authenticate();
        String authHeader = "Bearer " + account.getAccess_token();
        logger.info("Access token : {}", authHeader);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", authHeader);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        return httpHeaders;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    @Bean("device")
    public CustomDevice getDevice() throws DeviceRuntimeException {
        CustomDevice customDevice = deviceService.getDevice(CustomDevice.class);
        return customDevice;
    }
}
