package com.apartment.data_provider.core.apiclients.onliner;

import com.apartment.apiclient.enums.Param;
import com.apartment.apiclient.models.RequestParam;
import com.apartment.data_provider.configurations.OnlinerConfiguration;
import com.apartment.data_provider.core.interfaces.apiclients.onliner.SaleApartmentAll;
import com.apartment.data_provider.core.models.providers.onliner.OnlinerApartmentSale;
import com.apartment.data_provider.core.models.providers.onliner.OnlinerRequest;
import com.apartment.data_provider.core.models.providers.onliner.OnlinerResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.core5.http.Method;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.util.List;

@Component
public class OnlinerSaleApartmentAll
        extends OnlineApiClient<OnlinerRequest, OnlinerResponse<OnlinerApartmentSale>>
        implements SaleApartmentAll {

    private final ObjectMapper mapper;

    public OnlinerSaleApartmentAll(@Qualifier("socksHttpClient")HttpClient client,
                                   OnlinerConfiguration configuration,
                                   ObjectMapper mapper) {
        super(client, configuration.getSaleUrl(), Method.GET);
        this.mapper = mapper;
    }

    @Override
    protected OnlinerResponse<OnlinerApartmentSale> Converter(String body) throws JsonProcessingException {
        OnlinerResponse<OnlinerApartmentSale> apartments = mapper
                .readValue(body, new TypeReference<OnlinerResponse<OnlinerApartmentSale>>(){});
        return apartments;
    }

    @Override
    protected void setParameters(List<RequestParam> params, OnlinerRequest model) {
        super.setParameters(params, model);
        params.add(new RequestParam("page", model.page(), Param.QUERY));
    }

    @Override
    public OnlinerResponse<OnlinerApartmentSale> getSaleApartments(OnlinerRequest request) throws IOException, URISyntaxException, InterruptedException {
        return execute(request);
    }
}