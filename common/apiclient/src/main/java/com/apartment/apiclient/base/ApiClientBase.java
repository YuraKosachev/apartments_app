package com.apartment.apiclient.base;


import com.apartment.apiclient.enums.Param;
import com.apartment.apiclient.exceptions.DataServiceException;
import com.apartment.apiclient.interfaces.DataClient;
import com.apartment.apiclient.models.RequestParam;
import com.apartment.apiclient.models.RequestSetting;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import org.apache.hc.core5.http.Method;

import java.io.IOException;
import java.net.MalformedURLException;
import org.apache.hc.core5.http.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
public abstract class ApiClientBase<TRequest, TResponse> implements DataClient<TRequest, TResponse> {

    protected HttpClient client;
    protected String url;
    protected Method method;

    @Override
    public TResponse execute(TRequest model)
            throws IOException,
            URISyntaxException,
            InterruptedException {
        RequestSetting setting = getRequestSetting(url, model);
        URL uri = new URL(setting.getEndpoint());
        HttpRequest.BodyPublisher bodyPublisher = setting.getBody() == null || method.equals(Method.GET)
                ? HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.ofString(setting.getBody());

        HttpRequest.Builder builderRequest = HttpRequest.newBuilder()
                .uri(uri.toURI())
                .method(method.toString(), bodyPublisher);

        if (!setting.getHeaders().isEmpty()) {
            builderRequest.headers(setting.getHeaders().toArray(String[]::new));
        }

        HttpRequest request = builderRequest.build();

        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());
        return responseHandler(response);
    }

    private RequestSetting getRequestSetting(String endpoint, TRequest model) {
        List<RequestParam> requestParams = new ArrayList<>();
        setParameters(requestParams, model);

        List<String> bodyBuilder = new ArrayList<>();
        List<String> headersBuilder = new ArrayList<>();
        List<String> endpointBuilder = new ArrayList<>();

        String body = null;

        requestParams.sort(Comparator.comparing(
                (RequestParam r) -> r.getParam() != Param.OVERRIDE_URL
        ));
        for (RequestParam param : requestParams) {
            if(param.getParam() == Param.OVERRIDE_URL) {
                endpoint = param.getValue().toString();
            }
            if (param.getParam() == Param.BODY) {
                Object val = param.getValue();
                if (val instanceof String)
                    bodyBuilder.add("\"" + param.getName() + "\":\"" + param.getValue() + "\"");
                else
                    bodyBuilder.add("'" + param.getName() + "':" + param.getValue());
            }
            if (param.getParam() == Param.HEAD) {
                headersBuilder.add(param.getName());
                headersBuilder.add(param.getValue().toString());
            }
            if (param.getParam() == Param.QUERY) {
                endpointBuilder.add(param.getName() + "=" + param.getValue());
            }
        }

        if (!endpointBuilder.isEmpty()) {
            endpoint = endpoint + "?" + String.join("&", endpointBuilder);
        }

        if (!bodyBuilder.isEmpty()) {
            body = "{" + String.join(",", bodyBuilder) + "}";
        }

        return new RequestSetting(body, endpoint, headersBuilder);
    }

    protected void setParameters(List<RequestParam> params, TRequest model) {
    }


    @SneakyThrows
    protected TResponse responseHandler(HttpResponse<String> response) {
        if (!(response.statusCode() >= 200 && response.statusCode() < 300) && !canHandling(response)) {
            throw new DataServiceException(response.statusCode(), "Something went wrong! status code: " + response.statusCode());
        }
        return Converter(response.body());
    }

    protected abstract <TResponse> TResponse Converter(String body) throws JsonProcessingException;

    protected boolean canHandling(HttpResponse<String> response) {
        return false;
    }
}