package com.apartment.notification.core.apiclients;

import com.apartment.apiclient.base.ApiClientBase;
import com.apartment.apiclient.enums.Param;
import com.apartment.apiclient.models.RequestParam;
import com.apartment.notification.configurations.ApiConfiguration;
import com.apartment.notification.core.interfaces.apiclients.TelegramApiMessage;
import com.apartment.notification.core.models.dtos.TelegramMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.Method;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.util.List;

@Service

public class TelegramMessageApi extends ApiClientBase<TelegramMessageRequest, Integer>
        implements TelegramApiMessage {

    public TelegramMessageApi(HttpClient client, ApiConfiguration apiConfiguration) {
        super(client, apiConfiguration.getTeleframMessageApiFormat(), Method.GET);
    }



    @Override
    public Integer sendMessage(TelegramMessageRequest request) throws IOException, URISyntaxException, InterruptedException {
        return execute(request);
    }
    @Override
    protected void setParameters(List<RequestParam> params, TelegramMessageRequest model){
        params.add(new RequestParam("chat_id",model.getChatId() ,Param.QUERY));
        params.add(new RequestParam("parse_mode",model.getParseMode().getType(), Param.QUERY));
        params.add(new RequestParam("text", model.getMessage(),Param.QUERY));

    }

    @Override
    protected Integer Converter(String body) throws JsonProcessingException {
        return 200;
    }
}
