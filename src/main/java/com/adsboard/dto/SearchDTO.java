package com.adsboard.dto;

import lombok.*;

/**
 * DTO для передачи параметров поиска объявлений.
 * Инкапсулирует все параметры фильтрации и пагинации для поиска объявлений.
 * Используется в контроллерах для приема и передачи параметров поиска
 * между слоями приложения
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchDTO {

    private String searchTerm;
    private Long categoryId;
    private Long cityId;
    private Long regionId;
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortDirection;
}