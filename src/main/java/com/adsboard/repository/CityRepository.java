package com.adsboard.repository;

import com.adsboard.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Репозиторий для работы с сущностью City.
 * Предоставляет метода ля поиска городов по региону.
 */
@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    /**
     * Находит все города, принадлежащие указанному региону.
     * Используется для построения выпадающих списков городов при выборе
     * региона в формах создания/редактирования объявления
     */
    List<City> findByRegionId(Long regionId);
}