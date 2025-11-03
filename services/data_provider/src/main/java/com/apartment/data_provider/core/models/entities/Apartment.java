package com.apartment.data_provider.core.models.entities;

import com.apartment.data_provider.core.enums.GeoDataStatus;
import com.apartment.kafka.enums.ApartmentType;
import com.apartment.kafka.enums.Event;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "apartments")
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false, name = "apartment_type")
    ApartmentType apartmentType;
    @Column(nullable = false, name = "ref_id")
    int refId;
    @Column(nullable = false)
    String address;
    @Column(nullable = false, name = "user_address")
    String userAddress;
    @Column(nullable = false)
    double latitude;
    @Column(nullable = false)
    double longitude;
    @Column(nullable = false)
    double amount;
    @Column(nullable = false)
    String currency;
    String photo;

    @Column(nullable = false, name = "up_available_in")
    int upAvailableIn;
    @Column(nullable = false)
    String url;

    @Column(name = "author_id")
    Integer authorId;
    Boolean resale;
    @Column(name = "number_of_rooms")
    Integer numberOfRooms;
    Integer floor;
    @Column(name = "number_of_floors")
    Integer numberOfFloors;

    Double total;
    Double living;
    Double kitchen;

    @Column(name = "seller_type")
    String sellerType;

    @Column(name = "rent_type")
    String rentType;
    Boolean owner;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    Event event;

    //geo data
    @Column(nullable = true, name = "postcode")
    Integer postCode;
    String country;
    String neighbourhood;
    String county;
    String street;
    @Column(name = "country_code")
    String countryCode;
    String city;
    String district;
    String state;
    @Column(name = "state_code")
    String stateCode;
    String formatted;
    @Column(nullable = true, name = "house_number")
    String houseNumber;
    @Column(name = "box_lon1")
    Double boxLon1;
    @Column(name = "box_lat1")
    Double boxLat1;
    @Column(name = "box_lon2")
    Double boxLon2;
    @Column(name = "box_lat2")
    Double boxLat2;
    @Enumerated(EnumType.ORDINAL)
    GeoDataStatus geoDataStatus;
//end geo data

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_time_up", nullable = false)
    LocalDateTime lastTimeUp;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "app_created_at", nullable = false)
    @CreationTimestamp
    LocalDateTime appCreatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_sent_at")
    LocalDateTime lastSentAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        apartmentType = rentType == null
                ? ApartmentType.SALE
                : ApartmentType.RENT;

        if (event == null) {
            event = Event.INSERTED;
        }
        if(geoDataStatus == null) {
            geoDataStatus = GeoDataStatus.NEW;
        }
    }

}
