package com.adsboard.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityDTO {
    private Long id;

    @NotBlank(message = "Название города обязательно")
    private String name;
}
