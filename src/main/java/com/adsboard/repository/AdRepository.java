package com.adsboard.repository;

import com.adsboard.entity.Ad;
import com.adsboard.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Репозиторий для работы с сущностью Ad
 * Представляет широкий набор методов для поиска и фильтрации
 */
@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {

    /**
     * Находит все объявления с указанным статусом с пагинацией.
     * Используется для отображения активных объявлений на главной странице и в списке категорий.
     */
    Page<Ad> findAllByStatusId(Long statusId, Pageable pageable);

    /**
     * Находит все объявления указанного пользователя.
     * Используется в личном кабинете для отображения объявлений пользователя.
     */
    Page<Ad> findAllByUserId(Long userId, Pageable pageable);

    /**
     * Подсчитывает кол-во объявлений, сгруппированных по категориям.
     * Результат содержит массив объектов, где:
     * - [0] идентификатор категории (category.id)
     * - [1] количество объявлений COUNT(a.id)
     * Используется для отображения количества объявлений в навигации по категориям
     */
    @Query("SELECT a.category.id, COUNT(a.id) FROM Ad a GROUP BY a.category.id")
    List<Object[]> countAdsGroupedByCategory();

    /**
     * Находит объявления в указанной категории с пагинацией
     */
    Page<Ad> findByCategoryId(Long categoryId, Pageable pageable);

    /**
     * Находит объявления в указанном городе с пагинацией
     */
    Page<Ad> findByCityId(Long cityId, Pageable pageable);

    /**
     * Находит объявление по ID с подгрузкой изображений.
     * Использует LEFT JOIN FETCH для загрузки изображений в одном запросе,
     * избегая проблемы N+1
     */
    @Query("SELECT a FROM Ad a LEFT JOIN FETCH a.images WHERE a.id = :id")
    Ad findByIdWithImages(Long id);

    @Query("SELECT a from Ad a LEFT JOIN FETCH a.category LEFT JOIN FETCH a.city " +
            "WHERE a.status = :statusId AND (:searchTerm IS NULL OR " +
            "LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(a.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Ad> searchAds(@Param("statusId") Long statusId,
                       @Param("searchTerm") String searchTerm,
                       Pageable pageable);

    /**
     * Находит объявления по статусу и категории.
     * Подгружаются категории и город для оптимизации запросов.
     * Используется для фильтрации объявлений в определенной категории с учетом
     * их статуса (только активные)
     */
    @Query("SELECT a FROM Ad a LEFT JOIN FETCH a.category LEFT JOIN FETCH a.city " +
            "WHERE a.status = :statusId AND a.category = :categoryId")
    Page<Ad> findByStatusAndCategory(@Param("statusId") Long statusId,
                                     @Param("categoryId") Long categoryId,
                                     Pageable pageable);
}