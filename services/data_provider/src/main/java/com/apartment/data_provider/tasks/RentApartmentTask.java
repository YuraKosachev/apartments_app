package com.apartment.data_provider.tasks;

import com.apartment.data_provider.core.enums.EventType;
import com.apartment.data_provider.core.enums.GeoDataStatus;
import com.apartment.data_provider.core.interfaces.services.GeoApifyService;
import com.apartment.data_provider.core.interfaces.services.OnlinerService;
import com.apartment.data_provider.core.mappers.GeoApartmentMapper;
import com.apartment.data_provider.core.mappers.RentApartmentMapper;
import com.apartment.data_provider.core.models.entities.Apartment;
import com.apartment.data_provider.core.models.entities.EventLog;
import com.apartment.data_provider.core.models.providers.geoapify.GeoApifyRequest;
import com.apartment.data_provider.core.models.providers.onliner.OnlinerApartmentRent;
import com.apartment.data_provider.repositories.ApartmentRepository;
import com.apartment.data_provider.repositories.EventLogRepository;
import com.apartment.data_provider.tasks.base.BaseApartmentTask;
import com.apartment.kafka.enums.ApartmentType;
import com.apartment.kafka.enums.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class RentApartmentTask extends BaseApartmentTask {
    private final OnlinerService onlinerService;
    private final ApartmentRepository apartmentRepository;
    private final RentApartmentMapper rentApartmentMapper;
    private final EventLogRepository eventLogRepository;

    public RentApartmentTask(OnlinerService onlinerService,
                             ApartmentRepository apartmentRepository,
                             RentApartmentMapper rentApartmentMapper,
                             EventLogRepository eventLogRepository,
                             GeoApifyService geoApifyService,
                             GeoApartmentMapper geoApartmentMapper) {
        super(geoApifyService, geoApartmentMapper);
        this.onlinerService = onlinerService;
        this.apartmentRepository = apartmentRepository;
        this.rentApartmentMapper = rentApartmentMapper;
        this.eventLogRepository = eventLogRepository;
    }


    @Scheduled(cron = "${task.onliner.rent_cron}")
    public void run() {
        log.info("RentApartment task started");
        try {
            var apartments = apartmentRepository.getAllByTypes(Set.of(ApartmentType.RENT));
            log.info("RentApartment get source data");
            List<OnlinerApartmentRent> source = onlinerService.getRents();
            log.info("RentApartment get all source data");

            List<Apartment> inserted = new ArrayList<>();
            List<Apartment> updated = new ArrayList<>();

            for (OnlinerApartmentRent apartment : source) {
                var existing = apartments.stream()
                        .filter(item->item.getRefId() == apartment.getId())
                        .findFirst();
                if (existing.isPresent()) {

                    var entity = existing.get();
                    if(entity.getGeoDataStatus() != GeoDataStatus.PROCESSED
                            || !apartment.ToCompare(entity)
                            || entity.getDeletedAt() != null) {
                        rentApartmentMapper.toEntity(entity, apartment);
                        entity.setEvent(Event.UPDATED);
                        entity.setDeletedAt(null);

                        if(entity.getGeoDataStatus() != GeoDataStatus.PROCESSED) {
                            log.info("Geodata proccessing");
                            setGeoData(entity);
                        }
                        updated.add(entity);
                    }
                    continue;
                }
                var newEntity = rentApartmentMapper.toEntity(apartment);

                log.info("rent geodata proccessing... for -> lat: f% long:f5".formatted(newEntity.getLatitude(), newEntity.getLongitude()));
                setGeoData(newEntity);

                inserted.add(newEntity);
            }

            log.info("rent database activity started");
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
            log.info(String.format("RentApartment task finished inserted: %d deleted: %d, updated: %d source:%d", inserted.size(), deleted.size(), updated.size(), source.size()));
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
