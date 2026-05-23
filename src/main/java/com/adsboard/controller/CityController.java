package com.adsboard.controller;

import com.adsboard.dto.CityDTO;
import com.adsboard.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST API контроллер для работы с городами.
 * Используется для динамической загрузки городов на клиентской стороне.
 * Основные маршруты:
 *  - GET /api/cities/by-region/{regionId} - получение списко городов по региону
 */
@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    /**
     * Получение списка городов по ID региона
     * Для динамических выпадающих списков
     */
    @GetMapping("/by-region/{regionId}")
    public List<CityDTO> getCitiesByRegion(@PathVariable Long regionId) {
        return cityService.getCitiesByRegion(regionId).stream()
                .map(city -> new CityDTO(city.getId(), city.getName()))
                .collect(Collectors.toList());
    }
}
