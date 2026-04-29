package com.adsboard.dto;

import lombok.*;

/**
 * Для отображения с кол-вом объявлений
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
