package com.apartment.data_provider.core.interfaces.apiclients.onliner;

import com.apartment.data_provider.core.models.providers.onliner.OnlinerApartmentRent;
import com.apartment.data_provider.core.models.providers.onliner.OnlinerRequest;
import com.apartment.data_provider.core.models.providers.onliner.OnlinerResponse;

import java.io.IOException;
import java.net.URISyntaxException;

public interface RentApartmentAll {
    OnlinerResponse<OnlinerApartmentRent> getRentApartments(OnlinerRequest request) throws IOException, URISyntaxException, InterruptedException;
}
