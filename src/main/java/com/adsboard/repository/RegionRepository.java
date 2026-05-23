package com.adsboard.repository;

import com.adsboard.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью Region.
 * Предоставляет базовые crud-операции для регионов.
 */
@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

}