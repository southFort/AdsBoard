package com.adsboard.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Сущность, представляющая город, в котором подается объявление
 * Класс отображается на таблицу "cities" в БД и содержит информацию
 * о города с привязкой к региону
 */
@Entity
@Table(name = "cities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class City {

    /**
     * Уникальный идентификатор города
     * Генерируется автоматически с использованием последовательности "cities_id_seq"
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cities_id_seq")
    @SequenceGenerator(name = "cities_id_seq", sequenceName = "cities_id_seq", allocationSize = 1)
    private Long id;

    /**
     * Регион, в котором находится город
     * - Связь многие-к-одному с сущностью Region
     * - Загружается лениво для оптимизации производительности
     * - Не может быть null
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    /**
     * Название города
     * - Обязательное поле, не может быть null
     * - Максимальная длина - 100 символов
     */
    @Column(nullable = false, length = 100)
    private String name;
}