package com.adsboard.repository;

import com.adsboard.entity.AdStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностью AdStatus.
 * Предоставляет методы для доступа к справочнику статусов объявлений.
 */
@Repository
public interface AdStatusRepository extends JpaRepository<AdStatus, Long> {

    Optional<AdStatus> findByCode(String code);
}