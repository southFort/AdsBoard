package com.adsboard.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "regions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "regions_id_seq")
    @SequenceGenerator(name = "regions_id_seq", sequenceName = "regions_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<City> cities = new HashSet<>();
}