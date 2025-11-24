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

    private final int ATTEMPT_COUNT = 3;

    @Override
    public List<OnlinerApartmentSale> getSales() throws IOException, URISyntaxException, InterruptedException {
        List<OnlinerApartmentSale> toSale = new ArrayList<>();
        for (int page = 1; true; page++) {
            OnlinerResponse<OnlinerApartmentSale> info = null;
            int attempt = 0;

            do {
                try {
                    log.info("sale apartment page " + page + " parsing started; attempt:" + attempt);
                    info = saleApartmentAll.getSaleApartments(new OnlinerRequest(page));
                    attempt = ATTEMPT_COUNT;
                } catch (Exception e) {
                    log.error("sale request error -> " + e.getMessage());
                    attempt++;
                }
            } while (attempt < ATTEMPT_COUNT);


            if (info != null) {
                toSale.addAll(info.apartments());
            } else {
                log.info("sale request is null try next page ..");
                continue;
            }
            var size = info != null ? info.apartments().size() : 0;
            log.info("sale apartment page " + page + "/" + info.page().last() + " parsing completed. count items ->" + size);
            if (info.page().last() <= page) break;
        }
        return toSale;
    }

    @Override
    public List<OnlinerApartmentRent> getRents() throws IOException, URISyntaxException, InterruptedException {
        List<OnlinerApartmentRent> toRent = new ArrayList<>();
        for (int page = 1; true; page++) {
            OnlinerResponse<OnlinerApartmentRent> info = null;
            int attempt = 0;
            do {
                try {
                    log.info("rent apartment page " + page + " parsing started; attempt:" + attempt);
                    info = rentApartmentAll.getRentApartments(new OnlinerRequest(page));
                    attempt = ATTEMPT_COUNT;
                } catch (Exception e) {
                    log.error("rents request error -> " + e.getMessage());
                    attempt++;
                }
            } while (attempt < ATTEMPT_COUNT);

            if (info != null) {
                toRent.addAll(info.apartments());
            } else {
                log.info("rents request is null try next page ..");
                continue;
            }
            var size = info != null ? info.apartments().size() : 0;
            log.info("rent apartment page " + page + "/" + info.page().last() + " parsing completed. count items ->" + size);
            if (info.page().last() <= page) break;
        }
        return toRent;
    }
}
