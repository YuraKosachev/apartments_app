package com.apartment.apartment_api.core.models.entities;

import com.apartment.apartment_api.core.constants.DbTableConstants;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;

import java.util.UUID;
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name= DbTableConstants.POST_TABLE_NAME)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Postcode {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(unique = true, nullable = false)
    @EqualsAndHashCode.Include
    int postcode;

    @OneToMany(mappedBy = "postcode", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Address> addresses;
}
