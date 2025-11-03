package com.apartment.notification.core.interfaces.apiclients;

import com.apartment.notification.core.models.dtos.TelegramMessageRequest;
import com.apartment.notification.core.models.dtos.TelegramPhotoRequest;

import java.io.IOException;
import java.net.URISyntaxException;

public interface TelegramApiPhoto {
    Integer sendMessage(TelegramPhotoRequest request) throws IOException, URISyntaxException, InterruptedException;
}
