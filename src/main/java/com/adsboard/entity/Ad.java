package com.adsboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность, представляющая объявление в системе доски объявлений.
 * Класс отображается на таблицу "ads" в БД и содержит всю информацию
 * об объявлении: заголовок, описание, цену, местоположение, статус,
 * а так же связь с пользователем, категорией и изображениеми.
 * Особенности:
 * - Автоматическое управление полями createdAt b updatedAt xthtp JPA-колбэки
 * - Поддержка ленивой загрузки для связанных сущностей (User, Category, City, AdStatus)
 * - Каскадное удаление изображений при удалении объявления
 */
@Entity
@Table(name = "ads")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ad {

    /**
     * Уникальный идентификатор объявления
     * Генерируется автоматически с использованием последовательности БД "ads_id_seq"
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ads_id_seq")
    @SequenceGenerator(name = "ads_id_seq", sequenceName = "ads_id_seq", allocationSize = 1)
    private Long id;

    /**
     * Заголовок объявления
     * - Обязательное поле. не может быть null
     * - Максимальная длина - 200 символов
     */
    @Column(nullable = false, length = 200)
    private String title;

    /**
     * Полное описание объявления
     * - Обазательное поле, не может быть null
     * - Храниться в формате TEXT в БД, может содержать большие объмы текста
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    /**
     * Цена товара или услуги
     * - Обязательное поле, не может быть null
     * - Точность: 10 цифр всего, 2 цифры после запятой
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * Пользователь создавший объявление.
     * - Связь мноеги-к-одному с сущностью User
     * - Загружается лениво для оптимизации производительности
     * - Не может быть null
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Категория, к которой относится объявление
     * - Связь мноегие-к-одному с сущностью Category
     * - Загружается лениво для оптимизации производительности
     * - Не может быть null
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /**
     * Город, где подается объявление
     * - Связь мноегие-к-одному с сущностью City
     * - Загружается лениво для оптимизации производительности
     * - Не может быть null
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    /**
     * Статус объявления (активно, продано и т.п.)
     * - Связь мноегие-к-одному с сущностью AdStatus
     * - Загружается лениво для оптимизации производительности
     * - При создании нового объявления автоматически умтанаваливается статус с id == 11 (активно)
     * после реализации модуля модератора сменить на id = 12 (на модерации)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private AdStatus status;

    /**
     * Дата и время создания объявления
     * - Устанавливается автоматически перед сохранением см. onCreate()
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Дата и время последнего обновления объявления
     * - Обновляется автоматически перед каждым сохранениемя см. onUpload()
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Количество просмотров объявления
     * - По умолчанию устанавливается в 0 при создании
     */
    @Column(name = "view_count")
    private Integer viewCount = 0;

    /**
     * Список изображений, прикрепленных к объявлению.
     * - Связь один-ко-многим с сущностью AdImage
     * - Применяется каскадное удаление
     */
    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    private List<AdImage> images = new ArrayList<>();

    /**
     * JPA-колбэк, выполняется перед сохранением новой сущности
     * - createdAt - текущая дата и время
     * - updatedAt - текущая дата и время
     * - status - статус объявления, по умолчанию АКТИВНО (id = 11) если не указан
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = new AdStatus();
            status.setId(11L);
        }
    }

    /**
     * JPA-колбэк, выполняется перед обновлением существующей сущности
     * - Обновляет поле updatedAt текущей датой и временем
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}