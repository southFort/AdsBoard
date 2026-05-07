package com.adsboard.service;

import com.adsboard.entity.Region;
import com.adsboard.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;

    /**
     * Получить все регионы
     * @return список всех регионов
     */
    @Transactional(readOnly = true)
    public List<Region> getAllRegions(){
        return regionRepository.findAll();
    }
}
