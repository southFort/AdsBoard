package com.adsboard.service;

import com.adsboard.entity.City;
import com.adsboard.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Сервис для управления городами.
 * Предоставляет дизнес-логику для получения информации о городах
 * и фильтрации по регионам
 * Используется:
 * - В формах создания и редактирования объявлений
 * - Фильтрации объявлений по городу
 * - Отображении местоположения в объявлениях
 */
@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    /**
     * Получает список всех городов
     */
    @Transactional(readOnly = true)
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    /**
     * Получает список городов заданного региона
     */
    @Transactional(readOnly = true)
    public List<City> getCitiesByRegion(Long regionId) {
        return cityRepository.findByRegionId(regionId);
    }

    /**
     * Находит город по ID
     */
    @Transactional(readOnly = true)
    public City findById(Long id) {
        return cityRepository.findById(id).orElseThrow();
    }
}