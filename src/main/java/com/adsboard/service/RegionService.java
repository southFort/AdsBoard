package com.adsboard.service;

import com.adsboard.entity.Region;
import com.adsboard.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис для управления регионами.
 * Предоставляет бизнес-логику для получения информации о регионах
 * Регионы используются для группировки городов и
 * фильтрации объявлений по географическому признаку.
 */
@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;

    /**
     * Получает список всех регионов
     */
    @Transactional(readOnly = true)
    public List<Region> getAllRegions(){
        return regionRepository.findAll();
    }
}
