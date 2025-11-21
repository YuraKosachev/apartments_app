package com.apartment.data_provider.core.apiclients.onliner;

import com.apartment.apiclient.enums.Param;
import com.apartment.apiclient.models.RequestParam;
import com.apartment.data_provider.configurations.OnlinerConfiguration;
import com.apartment.data_provider.core.interfaces.apiclients.onliner.RentApartmentAll;
import com.apartment.data_provider.core.models.providers.onliner.OnlinerApartmentRent;
import com.apartment.data_provider.core.models.providers.onliner.OnlinerRequest;
import com.apartment.data_provider.core.models.providers.onliner.OnlinerResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.hc.core5.http.Method;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.util.List;

@Component
public class OnlinerRentApartmentAll
        extends OnlineApiClient<OnlinerRequest, OnlinerResponse<OnlinerApartmentRent>>
        implements RentApartmentAll {
    private final ObjectMapper mapper;
    public OnlinerRentApartmentAll(@Qualifier("socksHttpClient")HttpClient client,
                                   ObjectMapper mapper,
                                   OnlinerConfiguration configuration) {
        super(client, configuration.getRentUrl(), Method.GET);
        this.mapper = mapper;
    }

    @Override
    @SneakyThrows
    protected OnlinerResponse<OnlinerApartmentRent> Converter(String body) {
        OnlinerResponse<OnlinerApartmentRent> apartments = mapper
                .readValue(body, new TypeReference<OnlinerResponse<OnlinerApartmentRent>>(){});
        return apartments;
    }

    @Override
    protected void setParameters(List<RequestParam> params, OnlinerRequest model) {
        super.setParameters(params, model);
        params.add(new RequestParam("page", model.page(), Param.QUERY));
    }

    @Override
    public OnlinerResponse<OnlinerApartmentRent> getRentApartments(OnlinerRequest request)
            throws IOException, URISyntaxException, InterruptedException {
        return execute(request);
    }
}
