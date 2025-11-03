package com.apartment.notification.core.apiclients;

import com.apartment.apiclient.base.ApiClientBase;
import com.apartment.apiclient.enums.Param;
import com.apartment.apiclient.models.RequestParam;
import com.apartment.notification.configurations.ApiConfiguration;
import com.apartment.notification.core.interfaces.apiclients.TelegramApiMessage;
import com.apartment.notification.core.interfaces.apiclients.TelegramApiPhoto;
import com.apartment.notification.core.models.dtos.TelegramMessageRequest;
import com.apartment.notification.core.models.dtos.TelegramPhotoRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.hc.core5.http.Method;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.util.List;


@Service

public class TelegramPhotoApi extends ApiClientBase<TelegramPhotoRequest, Integer>
        implements TelegramApiPhoto {

    public TelegramPhotoApi(HttpClient client, ApiConfiguration apiConfiguration) {
        super(client, apiConfiguration.getTelegramImageApiFormat(), Method.POST);
    }



    @Override
    public Integer sendMessage(TelegramPhotoRequest request) throws IOException, URISyntaxException, InterruptedException {
        return execute(request);
    }
    @Override
    protected void setParameters(List<RequestParam> params, TelegramPhotoRequest model){

        params.add(new RequestParam("Content-Type","application/json" , Param.HEAD));
        params.add(new RequestParam("chat_id",model.getChatId() , Param.BODY));
        params.add(new RequestParam("parse_mode",model.getParseMode().getType(), Param.BODY));
        params.add(new RequestParam("caption", model.getCaption(), Param.BODY));

    }

    @Override
    protected Integer Converter(String body) throws JsonProcessingException {
        return 200;
    }
}