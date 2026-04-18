package com.adsboard.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdDTO {

    private Long id;

    @NotBlank(message = "Заголовок обязателен")
    @Size(max = 200, message = "Заголовок не должен превышать 200 символов")
    private String title;

    @NotBlank(message = "Описание обязателено")
    @Size(min = 10, message = "Описание должно содержать минимум 10 символов")
    private String description;

    @NotNull(message = "Цена обязательна")
    @DecimalMin(value = "0.01", message = "Цена должная быть больше 0")
    @DecimalMax(value = "99999999.99", message = "Цена слишком большая")
    private BigDecimal price;

    @NotNull(message = "Категория обязательна")
    private Long categoryId;

    @NotNull(message = "Город обязателен")
    private long cityId;

    private Long statusId;

    private String categoryName;
    private String cityName;
    private String statusName;
    private Integer viewCount;
}