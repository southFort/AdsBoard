package com.adsboard.repository;

import com.adsboard.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    /**
     * На момент создания админки добавить методы работы с регионами
     */
}