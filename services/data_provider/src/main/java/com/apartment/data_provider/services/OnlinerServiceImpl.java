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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class OnlinerServiceImpl implements OnlinerService {
//    @Qualifier("onlinerExecutor")
//    private final ExecutorService executor;

    private final SaleApartmentAll saleApartmentAll;
    private final RentApartmentAll rentApartmentAll;

    private final int ATTEMPT_COUNT = 3;
//
//    // new logic
//    private <T> OnlinerResponse<T> fetchPageWithRetry(Integer page, String serviceName, Function<Integer, OnlinerResponse<T>> requestFunc) {
//        int attempt = 0;
//        OnlinerResponse<T> info = null;
//
//        while (attempt < ATTEMPT_COUNT) {
//            try {
//                log.info("service {} apartment page {} parsing started; attempt:{}", serviceName, page, attempt);
//                info = requestFunc.apply(page);
//                break;
//            } catch (Exception e) {
//                log.error("service {} request error on page {} -> {}", serviceName, page, e.getMessage());
//                attempt++;
//            }
//        }
//
//        if (info == null) {
//            log.warn("service {} page {} failed after all attempts", serviceName, page);
//            return null;
//        }
//
//        log.info("service {} apartment page {} parsing completed. items -> {}", serviceName, page, info.apartments().size());
//        return info;
//    }
//
//    @Override
//    public List<OnlinerApartmentSale> getSales() throws IOException, URISyntaxException, InterruptedException {
//        int threads = 6; // количество потоков
//        return getApartments("Sale", threads, (x) -> {
//            try {
//                return saleApartmentAll.getSaleApartments(new OnlinerRequest(x.intValue()));
//            } catch (Exception e) {
//                log.error(e.getMessage());
//                throw new RuntimeException(e);
//            }
//        });
//    }
//
//    @Override
//    public List<OnlinerApartmentRent> getRents() throws IOException, URISyntaxException, InterruptedException {
//        int threads = 2; // количество потоков
//        return getApartments("Rent", threads, (x) -> {
//            try {
//                return rentApartmentAll.getRentApartments(new OnlinerRequest(x.intValue()));
//            } catch (Exception e) {
//                log.error(e.getMessage());
//                throw new RuntimeException(e);
//            }
//        });
//    }
//
//    private <T> List<T> getApartments(String serviceName,
//                                      int threads,
//                                      Function<Integer, OnlinerResponse<T>> requestFunc) {
//
//        CompletionService<OnlinerResponse<T>> completionService =
//                new ExecutorCompletionService<>(executor);
//
//        try {
//            log.info("{} apartments started", serviceName);
//
//            // Загружаем первую страницу
//            OnlinerResponse<T> first = requestFunc.apply(1);
//            if (first == null) {
//                log.error("{} first page FAILED", serviceName);
//                return List.of();
//            }
//
//            int lastPage = first.page().last();
//            List<T> result = new ArrayList<>(first.apartments());
//            List<Future<OnlinerResponse<T>>> futures = new ArrayList<>();
//
//            // Отправляем остальные страницы
//            for (int page = 2; page <= lastPage; page++) {
//                final int p = page;
//                futures.add(
//                        completionService.submit(() ->
//                                fetchPageWithRetry(p, serviceName, requestFunc)
//                        )
//                );
//            }
//
//            // Собираем результаты
//            for (int i = 0; i < futures.size(); i++) {
//                try {
//                    OnlinerResponse<T> response = completionService.take().get();
//                    if (response != null) {
//                        result.addAll(response.apartments());
//                    } else {
//                        log.warn("{}: received NULL response", serviceName);
//                    }
//                } catch (Exception e) {
//                    log.error("{} error retrieving page result: {}", serviceName, e.getMessage());
//                }
//            }
//
//            log.info("{} completed. total items = {}", serviceName, result.size());
//            return result;
//
//        } finally {
//            // больше НЕ выключаем executor !!!
//        }
//    }




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
