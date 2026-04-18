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

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {

    Page<Ad> findAllByStatusId(Long statusId, Pageable pageable);

    Page<Ad> findAllByUserId(Long userId, Pageable pageable);

    Page<Ad> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Ad> findByCityId(Long cityId, Pageable pageable);

    @Query("SELECT a FROM Ad a LEFT JOIN FETCH a.images WHERE a.id = :id")
    Ad findByIdWithImages(Long id);

    @Query("SELECT a FROM Ad a LEFT JOIN FETCH a.images WHERE a.status = :statusId")
    Page<Ad> findByStatusIdWithImages(@Param("statusId") Long statusId, Pageable pageable);

    @Query("SELECT a from Ad a LEFT JOIN FETCH a.category LEFT JOIN FETCH a.city " +
            "WHERE a.status = :statusId AND (:searchTerm IS NULL OR " +
            "LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(a.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Ad> searchAds(@Param("statusId") Long statusId,
                       @Param("searchTerm") String searchTerm,
                       Pageable pageable);

    @Query("SELECT a FROM Ad a LEFT JOIN FETCH a.category LEFT JOIN FETCH a.city " +
            "WHERE a.user = :userId AND (:searchTerm IS NULL OR " +
            "LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Ad> searchUserAds(@Param("userId") Long userId,
                           @Param("searchTerm") String searchTerm,
                           Pageable pageable);

    @Query("SELECT a FROM Ad a LEFT JOIN FETCH a.category LEFT JOIN FETCH a.city " +
            "WHERE a.status = :statusId AND a.category = :categoryId")
    Page<Ad> findByStatusAndCategory(@Param("statusId") Long statusId,
                                     @Param("categoryId") Long categoryId,
                                     Pageable pageable);

    List<Ad> findByUser(User user);
}