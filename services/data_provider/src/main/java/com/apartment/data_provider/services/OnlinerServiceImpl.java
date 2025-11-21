package com.apartment.data_provider.services;

import com.apartment.data_provider.core.interfaces.apiclients.onliner.RentApartmentAll;
import com.apartment.data_provider.core.interfaces.apiclients.onliner.SaleApartmentAll;
import com.apartment.data_provider.core.interfaces.services.OnlinerService;
import com.apartment.data_provider.core.models.providers.onliner.OnlinerApartmentRent;
import com.apartment.data_provider.core.models.providers.onliner.OnlinerApartmentSale;
import com.apartment.data_provider.core.models.providers.onliner.OnlinerRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OnlinerServiceImpl implements OnlinerService {
    private final SaleApartmentAll saleApartmentAll;
    private final RentApartmentAll rentApartmentAll;
    @Override
    public List<OnlinerApartmentSale> getSales() throws IOException, URISyntaxException, InterruptedException {
        List<OnlinerApartmentSale> toSale = new ArrayList<>();
        for(int page = 1; true; page++ ){
            log.info("sale apartment page " + page+ " parsing started");
            var info = saleApartmentAll.getSaleApartments(new OnlinerRequest(page));
            toSale.addAll(info.apartments());
            log.info("sale apartment page " + page+ "/"+ info.page().last() + " parsing completed. count items ->"+info.apartments().size());
            if(info.page().last() <= page) break;
        }
        return toSale;
    }

    @Override
    public List<OnlinerApartmentRent> getRents() throws IOException, URISyntaxException, InterruptedException {
        List<OnlinerApartmentRent> toRent = new ArrayList<>();
        for(int page = 1; true; page++ ){
            log.info("rent apartment page " + page+ " parsing started");
            var info = rentApartmentAll.getRentApartments(new OnlinerRequest(page));
            toRent.addAll(info.apartments());
            log.info("rent apartment page " + page+ "/"+ info.page().last() + " parsing completed. count items ->"+info.apartments().size());
            if(info.page().last() <= page) break;
        }
        return toRent;
    }
}
