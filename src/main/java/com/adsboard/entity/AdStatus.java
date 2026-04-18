package com.adsboard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ad_statuses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ad_statuses_id_seq")
    @SequenceGenerator(name = "ad_statuses_id_seq", sequenceName = "ad_statuses_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(name = "is_visible")
    private Boolean isVisible = true;
}
