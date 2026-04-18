package com.adsboard.dto;

import lombok.*;

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