package com.apartment.data_provider.tasks;

import com.apartment.data_provider.core.enums.EventType;
import com.apartment.data_provider.core.enums.GeoDataStatus;
import com.apartment.data_provider.core.interfaces.services.GeoApifyService;
import com.apartment.data_provider.core.interfaces.services.OnlinerService;
import com.apartment.data_provider.core.mappers.GeoApartmentMapper;
import com.apartment.data_provider.core.mappers.SaleApartmentMapper;
import com.apartment.data_provider.core.models.entities.Apartment;
import com.apartment.data_provider.core.models.entities.EventLog;
import com.apartment.data_provider.core.models.providers.onliner.OnlinerApartmentSale;
import com.apartment.data_provider.repositories.ApartmentRepository;
import com.apartment.data_provider.repositories.EventLogRepository;
import com.apartment.data_provider.tasks.base.BaseApartmentTask;
import com.apartment.kafka.enums.ApartmentType;
import com.apartment.kafka.enums.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class SaleApartmentTask extends BaseApartmentTask {
    private final OnlinerService onlinerService;
    private final ApartmentRepository apartmentRepository;
    private final SaleApartmentMapper saleApartmentMapper;
    private final EventLogRepository eventLogRepository;

    public SaleApartmentTask(OnlinerService onlinerService,
                             ApartmentRepository apartmentRepository,
                             SaleApartmentMapper saleApartmentMapper,
                             EventLogRepository eventLogRepository,
                             GeoApartmentMapper geoApartmentMapper,
                             GeoApifyService geoApifyService) {
        super(geoApifyService, geoApartmentMapper);
        this.onlinerService = onlinerService;
        this.apartmentRepository = apartmentRepository;
        this.saleApartmentMapper = saleApartmentMapper;
        this.eventLogRepository = eventLogRepository;
    }

    @Scheduled(cron = "${task.onliner.sale_cron}")
    public void run() {
        log.info("SaleApartment task started");
        var startTime = LocalDateTime.now();
        try {
            var apartments = apartmentRepository.getAllByTypes(Set.of(ApartmentType.SALE));
            log.info("SaleApartment get source data");
            List<OnlinerApartmentSale> source = onlinerService.getSales();
            if(source == null || source.isEmpty()) {
                log.info("SaleApartment source is null or empty");
                return;
            }
            List<Apartment> inserted = new ArrayList<>();
            List<Apartment> updated = new ArrayList<>();

            for (OnlinerApartmentSale apartment : source) {
                 var existing = apartments.stream()
                         .filter(item->item.getRefId() == apartment.getId())
                         .findFirst();
                 if (existing.isPresent()) {
                     var entity = existing.get();
                     if(entity.getGeoDataStatus() != GeoDataStatus.PROCESSED
                             || !apartment.ToCompare(entity) || entity.getDeletedAt() != null) {
                         saleApartmentMapper.toEntity(entity, apartment);
                         entity.setEvent(Event.UPDATED);
                         entity.setDeletedAt(null);
                         if(entity.getGeoDataStatus() != GeoDataStatus.PROCESSED) {
                             log.info("geodata proccessing");
                             setGeoData(entity);
                         }
                         updated.add(entity);
                     }
                     continue;
                 }
                 var newEntity = saleApartmentMapper.toEntity(apartment);
                log.info("sale geodata proccessing... for -> lat: f% long:f5".formatted(newEntity.getLatitude(), newEntity.getLongitude()));
                 setGeoData(newEntity);
                 inserted.add(newEntity);
            }
            log.info("sale database activity started");
            var sourceIds = source.stream().map(item->item.getId()).toList();
            List<Apartment> deleted = apartments.stream()
                    .filter(item->!sourceIds.contains(item.getRefId()) && item.getDeletedAt() == null)
                            .map(item->{
                                item.setEvent(Event.DELETED);
                                item.setDeletedAt(LocalDateTime.now());
                                return item;
                            })
                    .toList();

            if(!inserted.isEmpty()) {
                apartmentRepository.saveAll(inserted);
            }

            if(!updated.isEmpty()) {
                apartmentRepository.saveAll(updated);
            }

            if(!deleted.isEmpty()){
                apartmentRepository.saveAll(deleted);
            }
            var duration = Duration.between(startTime, LocalDateTime.now());
            var parsingTime = "%d h %d m.".formatted(duration.toHours(), duration.toMinutesPart());
            log.info(String.format("SaleApartment task finished inserted: %d deleted: %d, updated: %d source:%d. Parsing took -> %s", inserted.size(), deleted.size(), updated.size(), source.size(), parsingTime));
        }catch (Exception e){
            log.error(e.getMessage());
            var log = EventLog.builder()
                    .type(EventType.ERROR)
                    .content(e.getMessage())
                    .build();
            eventLogRepository.save(log);
        }
    }


}
