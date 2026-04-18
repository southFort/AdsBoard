package com.adsboard.repository;

import com.adsboard.entity.AdImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdImageRepository extends JpaRepository<AdImage, Long> {

    List<AdImage> findByAdId(Long adId);

    AdImage findByAdIdAndIsMainTrue(Long adId);
}
