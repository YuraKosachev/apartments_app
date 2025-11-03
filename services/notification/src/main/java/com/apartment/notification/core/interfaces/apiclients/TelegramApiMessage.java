package com.apartment.notification.core.interfaces.apiclients;

import com.apartment.notification.core.models.dtos.TelegramMessageRequest;

import java.io.IOException;
import java.net.URISyntaxException;

public interface TelegramApiMessage {
    Integer sendMessage(TelegramMessageRequest request) throws IOException, URISyntaxException, InterruptedException;
}
