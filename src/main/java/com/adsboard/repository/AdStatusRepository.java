package com.adsboard.repository;

import com.adsboard.entity.AdStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdStatusRepository extends JpaRepository<AdStatus, Long> {

    Optional<AdStatus> findByCode(String code);
}