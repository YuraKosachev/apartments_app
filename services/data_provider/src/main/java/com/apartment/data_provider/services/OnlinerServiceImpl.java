package com.apartment.data_provider.services;

import com.apartment.data_provider.core.interfaces.apiclients.onliner.RentApartmentAll;
import com.apartment.data_provider.core.interfaces.apiclients.onliner.SaleApartmentAll;
import com.apartment.data_provider.core.interfaces.services.OnlinerService;
import com.apartment.data_provider.core.models.providers.onliner.OnlinerApartmentRent;
import com.apartment.data_provider.core.models.providers.onliner.OnlinerApartmentSale;
import com.apartment.data_provider.core.models.providers.onliner.OnlinerRequest;
import com.apartment.data_provider.core.models.providers.onliner.OnlinerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OnlinerServiceImpl implements OnlinerService {
    private final SaleApartmentAll saleApartmentAll;
    private final RentApartmentAll rentApartmentAll;

    @Override
    public List<OnlinerApartmentSale> getSales() throws IOException, URISyntaxException, InterruptedException {
        List<OnlinerApartmentSale> toSale = new ArrayList<>();
        for (int page = 1; true; page++) {
            log.info("sale apartment page " + page + " parsing started");
            OnlinerResponse<OnlinerApartmentSale> info = null;
            try {
                info = saleApartmentAll.getSaleApartments(new OnlinerRequest(page));
            }catch (Exception e) {
                log.error("sale request error -> " + e.getMessage());
            }
            if (info != null) {
                toSale.addAll(info.apartments());
            }
            else{
                log.info("sale request is null try next page ..");
            }
            log.info("sale apartment page " + page + "/" + info.page().last() + " parsing completed. count items ->" + info.apartments().size());
            if (info.page().last() <= page) break;
        }
        return toSale;
    }

    @Override
    public List<OnlinerApartmentRent> getRents() throws IOException, URISyntaxException, InterruptedException {
        List<OnlinerApartmentRent> toRent = new ArrayList<>();
        for (int page = 1; true; page++) {
            log.info("rent apartment page " + page + " parsing started");
            OnlinerResponse<OnlinerApartmentRent> info = null;
            try {
                info = rentApartmentAll.getRentApartments(new OnlinerRequest(page));
            } catch (Exception e) {
                log.error("rents request error -> " + e.getMessage());
            }
            if (info != null) {
                toRent.addAll(info.apartments());
            } else {
                log.info("rents request is null try next page ..");
            }
            log.info("rent apartment page " + page + "/" + info.page().last() + " parsing completed. count items ->" + info.apartments().size());
            if (info.page().last() <= page) break;
        }
        return toRent;
    }
}
