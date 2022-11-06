package com.ioto.device.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotImplementedException extends DeviceRuntimeException {

    private static final long serialVersionUID = 1L;

    public NotImplementedException(){
        super("Method Not Implemented");
    }
}
