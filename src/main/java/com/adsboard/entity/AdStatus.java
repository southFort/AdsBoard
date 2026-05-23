package com.adsboard.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Сущность, представляющая статус объявления (активно, продано, архивировано и т.д.)
 * Класс отображается на таблицу "ad_statuses" в БД и содержит справочную информацию
 * о возможных статусах объявлений
 */
@Entity
@Table(name = "ad_statuses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdStatus {

    /**
     * Уникальный идентификатор объявления
     * Генерируется автоматически с использованием последовательности БД "ad_statuses_id_seq"
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ad_statuses_id_seq")
    @SequenceGenerator(name = "ad_statuses_id_seq", sequenceName = "ad_statuses_id_seq", allocationSize = 1)
    private Long id;

    /**
     * Уникальный код статуса
     * - Обязательное поле не может быть null
     * - Должно быть уникальным в пределах таблицы
     * - Максимальная длина - 50 символов
     * Примеры: "ACTIVE", "SOLD" и т.д.
     */
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    /**
     * Человеко-читаемое название статуса
     * - Обязательное поле не может быть null
     * - Максимальная длина - 100 символов
     * Примеры: "Активно", "Продано" и т.д.
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Описание статуса с дополнительной информацией
     * - Максимальная длина - 255 символов
     */
    @Column(length = 255)
    private String description;

    /**
     * Флаг, определяющий, видно ли объявление с данным статусом пользователям
     */
    @Column(name = "is_visible")
    private Boolean isVisible = true;
}
