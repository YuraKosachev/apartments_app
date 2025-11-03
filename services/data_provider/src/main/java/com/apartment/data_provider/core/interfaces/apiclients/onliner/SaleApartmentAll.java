package com.apartment.data_provider.core.interfaces.apiclients.onliner;

import com.apartment.data_provider.core.models.providers.onliner.OnlinerApartmentSale;
import com.apartment.data_provider.core.models.providers.onliner.OnlinerRequest;
import com.apartment.data_provider.core.models.providers.onliner.OnlinerResponse;

import java.io.IOException;
import java.net.URISyntaxException;

public interface SaleApartmentAll {
    OnlinerResponse<OnlinerApartmentSale> getSaleApartments(OnlinerRequest request) throws IOException, URISyntaxException, InterruptedException;
}
