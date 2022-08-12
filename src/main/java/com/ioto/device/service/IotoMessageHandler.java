package com.ioto.device.service;

import com.ioto.device.model.message.IotoMessage;

public interface IotoMessageHandler {
    void onMessageReceived(IotoMessage message);
}
