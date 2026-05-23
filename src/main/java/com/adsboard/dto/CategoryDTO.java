package com.adsboard.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.util.List;

/**
 * DTO для передачи данных о категории объявлений.
 * Поддерживает древовидную структуру категорий для построения иерархической
 * навигации в пользовательском интерфейсе.
 * Используется:
 * - Для отображения дерева категорий в навигационном меню
 * - Создания и редактирования категорий, когда реализуем админ панель
 * - передачи данных о категориях с их дочерними элементами
 *
 * - Содержат рекурсивную структуру children для построения дерева
 * - Включает информацию о родительской категории для навигации "хлебные крошки"
 * - Поддерживает подсчет количества объявлений в категории
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {

    private Long id;

    @NotBlank(message = "Название категории обязательно")
    @Size(max = 100, message = "Название не должно превышать 100 символов")
    private String name;

    private Long parentId;
    private String parentName;

    private List<CategoryDTO> children;

    private Integer adsCount;

    @Builder.Default
    private Boolean isActive = true;
}
