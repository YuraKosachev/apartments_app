package com.apartment.data_provider.core.interfaces.services;

import com.apartment.data_provider.core.models.providers.onliner.OnlinerApartmentRent;
import com.apartment.data_provider.core.models.providers.onliner.OnlinerApartmentSale;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface OnlinerService {
    List<OnlinerApartmentSale> getSales() throws IOException, URISyntaxException, InterruptedException;
    List<OnlinerApartmentRent> getRents() throws IOException, URISyntaxException, InterruptedException;
}
