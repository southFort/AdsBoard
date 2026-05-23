package com.adsboard.dto;

import lombok.*;

/**
 * Специализированный DTO для отображения категорий с кол-вом объявлений.
 * Используется для оптимизации запросов, когда необходимо отобразить
 * список категорий с количеством объявлений без загрузки полной иерархии.
 *
 * Отличие от CategoryDTO:
 * - Не содержит рекурсивную структуру
 * - Включает поле иконки категории
 * - Оптимизирован для отображения в списках и выпадающих меню
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryWithAdsCountDTO {

    private Long id;
    private String name;
    private Long parentId;
    private Integer adsCount;
    private String icon;
}
