package com.adsboard.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для передачи данных о городе.
 * Используется для выпадающих списков и отображении местоположения объявлений.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityDTO {
    private Long id;

    @NotBlank(message = "Название города обязательно")
    private String name;
}
