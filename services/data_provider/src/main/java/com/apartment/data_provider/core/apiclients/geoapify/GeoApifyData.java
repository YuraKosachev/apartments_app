package com.apartment.data_provider.core.apiclients.geoapify;

import com.apartment.apiclient.base.ApiClientBase;
import com.apartment.apiclient.enums.Param;
import com.apartment.apiclient.models.RequestParam;
import com.apartment.data_provider.configurations.GeoApifyConfiguration;
import com.apartment.data_provider.core.interfaces.apiclients.geoapify.GeoApifyGeoCoding;

import com.apartment.data_provider.core.models.providers.geoapify.GeoApifyRequest;
import com.apartment.data_provider.core.models.providers.geoapify.GeoApifyResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.core5.http.Method;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.util.List;

@Component
public class GeoApifyData
        extends ApiClientBase<GeoApifyRequest, GeoApifyResponse>
        implements GeoApifyGeoCoding
{
    private final ObjectMapper mapper;
    private final GeoApifyConfiguration config;

    public GeoApifyData(HttpClient client, GeoApifyConfiguration config, ObjectMapper mapper) {
        super(client, config.getUrl(), Method.GET);
        this.config = config;
        this.mapper = mapper;
    }

    @Override
    protected  GeoApifyResponse Converter(String body)
            throws JsonProcessingException {
        GeoApifyResponse response = mapper
                .readValue(body, GeoApifyResponse.class);
        return response;
    }

    @Override
    protected void setParameters(List<RequestParam> params, GeoApifyRequest model) {
        super.setParameters(params, model);
        params.add(new RequestParam("lat", model.getLatitude(), Param.QUERY));
        params.add(new RequestParam("lon", model.getLongitude(), Param.QUERY));
        params.add(new RequestParam("lang", "ru", Param.QUERY));
        params.add(new RequestParam("format", "json", Param.QUERY));
        params.add(new RequestParam("apiKey", config.getApiKey(), Param.QUERY));
    }

    @Override
    public GeoApifyResponse geoCoding(GeoApifyRequest request)
            throws IOException,
            URISyntaxException,
            InterruptedException {
        return execute(request);
    }
}
