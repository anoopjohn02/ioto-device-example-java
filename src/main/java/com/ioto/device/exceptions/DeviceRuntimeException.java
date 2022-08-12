package com.ioto.device.exceptions;

public class DeviceRuntimeException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public DeviceRuntimeException(final String string) {
    super(string);
  }

  public DeviceRuntimeException(final String string, final Throwable cause) {
    super(string, cause);
  }
}
