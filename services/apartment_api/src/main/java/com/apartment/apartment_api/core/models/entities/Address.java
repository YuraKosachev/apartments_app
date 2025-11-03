package com.apartment.apartment_api.core.models.entities;

import com.apartment.apartment_api.core.constants.DbTableConstants;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = DbTableConstants.ADDRESSE_TABLE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne
    @JoinColumn(name = "postcode_id")
    @EqualsAndHashCode.Include
    Postcode postcode;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "address", cascade = CascadeType.DETACH)
    List<Apartment> apartments;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id")
    @EqualsAndHashCode.Include
    City city;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id", nullable = false)
    @EqualsAndHashCode.Include
    Country country;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "county_id")
    @EqualsAndHashCode.Include
    County county;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "state_id")
    @EqualsAndHashCode.Include
    State state;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id")
    @EqualsAndHashCode.Include
    District district;

    @Column(nullable = false)
    String formatted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "street_id")
    @EqualsAndHashCode.Include
    Street street;

    @Column(nullable = false)
    @EqualsAndHashCode.Include
    Double latitude;

    @Column(nullable = false)
    @EqualsAndHashCode.Include
    Double longitude;

    @Column(name = "box_longitude1")
    @EqualsAndHashCode.Include
    Double boxLon1;

    @Column(name = "box_latitude1")
    @EqualsAndHashCode.Include
    Double boxLat1;

    @Column(name = "box_longitude2")
    @EqualsAndHashCode.Include
    Double boxLon2;

    @Column(name = "box_latitude2")
    @EqualsAndHashCode.Include
    Double boxLat2;

    @Column(name = "house_number")
    @EqualsAndHashCode.Include
    String houseNumber;
}
