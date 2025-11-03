package com.apartment.apiclient.interfaces;

import com.apartment.apiclient.exceptions.DataServiceException;

import java.io.IOException;
import java.net.URISyntaxException;

public interface DataClient<TRequest,TResponse> {
    TResponse execute(TRequest model) throws URISyntaxException, IOException, InterruptedException, DataServiceException;
}
