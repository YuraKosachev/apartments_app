package com.apartment.apartment_api.services;

import com.apartment.apartment_api.core.models.entities.Apartment;
import com.apartment.apartment_api.repositories.ApartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApartmentService {
    private final ApartmentRepository apartmentRepository;

    public <T> List<T> getApartments(Specification<Apartment> specification, Function<Apartment,T> mapper) {
        return apartmentRepository.findAll(specification).stream().map(mapper).collect(Collectors.toList());
    }
}
