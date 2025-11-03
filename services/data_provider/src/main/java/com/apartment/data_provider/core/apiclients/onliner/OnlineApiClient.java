package com.apartment.data_provider.core.apiclients.onliner;

import com.apartment.apiclient.base.ApiClientBase;

import com.apartment.apiclient.enums.Param;
import com.apartment.apiclient.models.RequestParam;
import org.apache.hc.core5.http.Method;

import java.net.http.HttpClient;
import java.util.List;

public abstract class OnlineApiClient<TRequest, TResponse>
        extends ApiClientBase<TRequest, TResponse> {
    public OnlineApiClient(HttpClient client, String url, Method method) {
        super(client, url, method);
    }

    @Override
    protected void setParameters(List<RequestParam> params, TRequest model) {
        super.setParameters(params, model);
        params.add(new RequestParam("bounds[lb][lat]", 50.736455137010665, Param.QUERY));
        params.add(new RequestParam("bounds[lb][long]", 18.577881529927257, Param.QUERY));
        params.add(new RequestParam("bounds[rt][lat]", 56.65622649350222, Param.QUERY));
        params.add(new RequestParam("bounds[rt][long]",39.46289129555226, Param.QUERY ));
        params.add(new RequestParam("accept", "application/json, text/plain, */*", Param.HEAD));
    }
}
