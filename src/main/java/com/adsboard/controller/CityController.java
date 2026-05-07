package com.adsboard.controller;

import com.adsboard.dto.CityDTO;
import com.adsboard.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * API для городов
 */
@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    /**
     * Получение списка городов по региону
     *
     * @param regionId
     * @return список  по региону
     */
    @GetMapping("/by-region/{regionId}")
    public List<CityDTO> getCitiesByRegion(@PathVariable Long regionId) {
        return cityService.getCitiesByRegion(regionId).stream()
                .map(city -> new CityDTO(city.getId(), city.getName()))
                .collect(Collectors.toList());
    }
}
