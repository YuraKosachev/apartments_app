package com.apartment.apartment_api.core.kafka;

import com.apartment.apartment_api.core.models.entities.*;
import com.apartment.apartment_api.repositories.*;
import com.apartment.kafka.constants.KafkaConstants;
import com.apartment.kafka.enums.Event;
import com.apartment.kafka.models.ApartmentMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class Consumer {
    private final ApartmentRepository apartmentRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final DistrictRepository districtRepository;
    private final PostcodeRepository postcodeRepository;
    private final StateRepository stateRepository;
    private final StreetRepository streetRepository;
    private final CountyRepository countyRepository;
    private final TaskApartmentRepository taskApartmentRepository;
    private final AddressRepository addressRepository;
    private final PriceRepository priceRepository;

    private final Map<String, String> countryMaps = Map.of("Белоруссия", "Беларусь");

    @Transactional
    public void handleData(ApartmentMessage message) {
        var source = message.getApartments();
        if (source == null || source.length <= 0)
            return;

        var incomeCities = Arrays.stream(source)
                .filter(a -> a.geoInfo() != null)
                .map(a -> a.geoInfo().city())
                .collect(Collectors.toSet());

        var incomeCountries = Arrays.stream(source)
                .filter(a -> a.geoInfo() != null && a.geoInfo().country() != null)
                .map(a -> countryMaps.containsKey(a.geoInfo().country())
                        ? countryMaps.get(a.geoInfo().country())
                        : a.geoInfo().country()
                ).collect(Collectors.toSet());

        var incomeDistricts = Arrays.stream(source)
                .filter(a -> a.geoInfo() != null && a.geoInfo().district() != null)
                .map(a -> a.geoInfo().district())
                .collect(Collectors.toSet());

        var incomePostcodes = Arrays.stream(source)
                .filter(a -> a.geoInfo() != null && a.geoInfo().postCode() != null)
                .map(a -> a.geoInfo().postCode())
                .collect(Collectors.toSet());

        var incomeStates = Arrays.stream(source)
                .filter(a -> a.geoInfo() != null && a.geoInfo().state() != null)
                .map(a -> a.geoInfo().state()).collect(Collectors.toSet());

        var incomeCounties = Arrays.stream(source)
                .filter(a -> a.geoInfo() != null && a.geoInfo().county() != null)
                .map(a -> a.geoInfo().county()).collect(Collectors.toSet());

        var incomeStreets = Arrays.stream(source)
                .filter(a -> a.geoInfo() != null && a.geoInfo().street() != null)
                .map(a -> a.geoInfo().street()).collect(Collectors.toSet());

        var refIds = Arrays.stream(source)
                .map(a -> a.id()).collect(Collectors.toSet());

        var existingApartments = apartmentRepository.getAllByRefIds(refIds);
        // get all addresses
        var addresses = addressRepository.findAll();

        var cities = saveNewAndGetAll(incomeCities, cityRepository,
                City::getName,
                (n) -> City.builder().name(n).build());

        var countries = saveNewAndGetAll(incomeCountries, countryRepository,
                Country::getName,
                (n) -> Country.builder().name(n).build());

        var districts = saveNewAndGetAll(incomeDistricts, districtRepository,
                District::getName,
                (n) -> District.builder().name(n).build());

        var postcodes = saveNewAndGetAll(incomePostcodes, postcodeRepository,
                Postcode::getPostcode,
                (c) -> Postcode.builder().postcode(c).build());

        var states = saveNewAndGetAll(incomeStates, stateRepository,
                State::getName,
                (c) -> State.builder().name(c).build());

        var counties = saveNewAndGetAll(incomeCounties, countyRepository,
                County::getName,
                (c) -> County.builder().name(c).build());

        var streets = saveNewAndGetAll(incomeStreets, streetRepository,
                Street::getName,
                (s) -> Street.builder().name(s).build());

        for (var sourceApartment : source) {
            var existing = existingApartments.stream()
                    .filter(a -> a.getSourceId().equals(sourceApartment.id()))
                    .findFirst();

            if (existing.isEmpty()) {
                //TODO if present
                var city = cities.stream().filter(c -> sourceApartment.geoInfo().city() != null
                        && c.getName().equals(sourceApartment.geoInfo().city())).findFirst();

                var country = countries.stream().filter(c -> sourceApartment.geoInfo().country() != null
                        && c.getName().equals(countryMaps.containsKey(sourceApartment.geoInfo().country())
                        ? countryMaps.get(sourceApartment.geoInfo().country())
                        : sourceApartment.geoInfo().country())).findFirst();

                var district = districts.stream().filter(d -> sourceApartment.geoInfo().district() != null
                        && d.getName().equals(sourceApartment.geoInfo().district())).findFirst();

                var postcode = postcodes.stream().filter(d -> sourceApartment.geoInfo().postCode() != null
                        && d.getPostcode() == sourceApartment.geoInfo().postCode()).findFirst();

                var state = states.stream().filter(s -> sourceApartment.geoInfo().state() != null
                        && s.getName().equals(sourceApartment.geoInfo().state())).findFirst();

                var county = counties.stream().filter(c -> sourceApartment.geoInfo().county() != null
                        && c.getName().equals(sourceApartment.geoInfo().county())).findFirst();

                var street = streets.stream().filter(c -> sourceApartment.geoInfo().street() != null
                        && c.getName().equals(sourceApartment.geoInfo().street())).findFirst();


                var address = addresses.stream()
                        .filter(a -> a.getFormatted().equals(sourceApartment.geoInfo().formatted()))
                        .findFirst().orElse(null);
                if (address == null) {
                    var newAddress = Address.builder()
                            .boxLon2(sourceApartment.geoInfo().boxLon2())
                            .boxLat2(sourceApartment.geoInfo().boxLat2())
                            .boxLon1(sourceApartment.geoInfo().boxLon1())
                            .boxLat1(sourceApartment.geoInfo().boxLat1())
                            .formatted(sourceApartment.geoInfo().formatted())
                            .houseNumber(String.valueOf(sourceApartment.geoInfo().houseNumber()))
                            .latitude(sourceApartment.propertyInfo().latitude())
                            .longitude(sourceApartment.propertyInfo().longitude())
                            .street(street.orElse(null))
                            .postcode(postcode.orElse(null))
                            .county(county.orElse(null))
                            .city(city.orElse(null))
                            .country(country.orElse(null))
                            .district(district.orElse(null))
                            .state(state.orElse(null))
                            .build();


                    address = addressRepository.save(newAddress);
                }


                var apartment = Apartment.builder()
                        .total(sourceApartment.propertyInfo().total())
                        .address(address)
                        .photo(sourceApartment.propertyInfo().photo())
                        .url(sourceApartment.propertyInfo().url())
                        .lastTimeUp(sourceApartment.lastTimeUp())
                        .resale(sourceApartment.propertyInfo().resale())
                        .floor(sourceApartment.propertyInfo().floor())
                        .owner(sourceApartment.propertyInfo().owner())
                        .sourceId(sourceApartment.id())
                        .living(sourceApartment.propertyInfo().living())
                        .kitchen(sourceApartment.propertyInfo().kitchen())
                        .apartmentType(sourceApartment.apartmentType())
                        .numberOfRooms(sourceApartment.propertyInfo().numberOfRooms())
                        .deletedAt(sourceApartment.event() == Event.DELETED ? LocalDateTime.now() : null)
                        .numberOfFloors(sourceApartment.propertyInfo().numberOfFloors())
                        .rentType(sourceApartment.propertyInfo().rentType())
                        .createdAt(sourceApartment.createdAt())
                        .build();
                apartmentRepository.save(apartment);

                var price = Price.builder()
                        .amount(sourceApartment.price().amount())
                        .apartment(apartment)
                        .currency(sourceApartment.price().currency()).build();

                priceRepository.save(price);
                continue;
            }

            var apartment = existing.get();
            if (sourceApartment.event() == Event.DELETED) {
                apartment.setDeletedAt(LocalDateTime.now());
                apartmentRepository.save(apartment);
                continue;
            }

            if (apartment.getDeletedAt() != null) {
                apartment.setDeletedAt(null);
            }

            //check price
            var prices = apartment.getPrices();
            var lastPrice = prices.stream()
                    .sorted(Comparator.comparing(Price::getCreatedAt).reversed())
                    .findFirst();
            if (lastPrice.isPresent()
                    && lastPrice.get().getAmount() != sourceApartment.price().amount()) {
                var newPrice = Price.builder()
                        .amount(sourceApartment.price().amount())
                        .apartment(apartment)
                        .currency(sourceApartment.price().currency())
                        .build();
                taskApartmentRepository.deleteAllByApartmentId(apartment.getId());
                priceRepository.save(newPrice);
            }

            apartmentRepository.save(apartment);
        }
    }


    private <T, R extends JpaRepository<RT, UUID>, RT> List<RT> saveNewAndGetAll(
            Set<T> source,
            R repository,
            Function<RT, T> tMapper,
            Function<T, RT> rMapper
    ) {
        var existingItems = repository.findAll().stream().map(tMapper).collect(Collectors.toSet());
        var newItems = source.stream().filter(i -> i != null && !existingItems.contains(i))
                .map(rMapper).toList();
        if (!newItems.isEmpty()) {
            repository.saveAll(newItems);
        }
        return repository.findAll();

    }

    @KafkaListener(topics = KafkaConstants.APARTMENT_TOPIC)
    public void consume(ApartmentMessage message) {
        try {
            log.info("Consuming message...");
            handleData(message);
            log.info("Consuming message succeeded...");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
