package com.apartment.apartment_api.core.models.entities;

import com.apartment.apartment_api.core.constants.DbTableConstants;
import com.apartment.kafka.enums.ApartmentType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name= DbTableConstants.APARTMENT_TABLE_NAME)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false, name = "apartment_type")
    ApartmentType apartmentType;
    @Column(nullable = false, name = "source_id")
    UUID sourceId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    Address address;

    String photo;
    @Column(nullable = false)
    String url;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "apartment", cascade = CascadeType.ALL)
    List<Price> prices;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_time_up", nullable = false)
    LocalDateTime lastTimeUp;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    LocalDateTime deletedAt;

    @OneToMany(mappedBy = "apartment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<TaskApartment> taskApartments;
}
