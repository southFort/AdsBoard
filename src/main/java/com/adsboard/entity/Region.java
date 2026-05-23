package com.adsboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Сущность, представляющая регион (область, край, республику т .п.)
 * Класс отображается на таблицу "regions" в БД и содержит информацию
 * о регионах с привязкой к городам
 * - Каскадное удаление городов при удалении региона
 */
@Entity
@Table(name = "regions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Region {

    /**
     * Уникальный идентификатор региона
     * Генерируется автоматически с использованием последовательности "regions_id_seq"
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "regions_id_seq")
    @SequenceGenerator(name = "regions_id_seq", sequenceName = "regions_id_seq", allocationSize = 1)
    private Long id;

    /**
     * Название региона
     * - Обязательное поле, не может быть null
     * - Максимальная длина - 100 символов
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Коллекция городов, входящих в регион
     * - Связь один-ко-многим с сущностью City
     * - Применяется каскадное удаление, orphanRemoval гарантирует удаление городов без родителя
     */
    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<City> cities = new HashSet<>();
}