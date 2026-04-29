package com.adsboard.service;

import com.adsboard.entity.Ad;
import com.adsboard.entity.AdImage;
import com.adsboard.entity.User;
import com.adsboard.dto.AdDTO;
import com.adsboard.dto.SearchDTO;
import com.adsboard.repository.AdRepository;
import com.adsboard.repository.AdStatusRepository;
import com.adsboard.repository.CategoryRepository;
import com.adsboard.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdService {

    private final AdRepository adRepository;
    private final AdStatusRepository adStatusRepository;
    private final CategoryRepository categoryRepository;
    private final CityRepository cityRepository;

    private static final String UPLOAD_DIR = "uploads/ads/";

    @Transactional(readOnly = true)
    public Page<Ad> findAllPublicAds(int page, int size) {
        return adRepository.findAllByStatusId(1L,
                PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }

    @Transactional(readOnly = true)
    public Page<Ad> findUserAds(Long userId, int page, int size) {
        return adRepository.findAllByUserId(userId,
                PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }

    @Transactional(readOnly = true)
    public Ad findById(Long id) {
        Ad ad = adRepository.findByIdWithImages(id);
        if (ad != null) {
            ad.setViewCount(ad.getViewCount() + 1);
            adRepository.save(ad);
        }
        return ad;
    }

    @Transactional
    public Ad creareAd(AdDTO adDTO, User user, List<MultipartFile> images) throws IOException {
        Ad ad = new Ad();
        ad.setTitle(adDTO.getTitle());
        ad.setDescription(adDTO.getDescription());
        ad.setPrice(adDTO.getPrice());
        ad.setUser(user);
        ad.setCategory(categoryRepository.findById(adDTO.getCategoryId()).orElseThrow());
        ad.setCity(cityRepository.findById(adDTO.getCityId()).orElseThrow());
        ad.setStatus(adStatusRepository.findById(1L).orElseThrow());

        Ad saveAd = adRepository.save(ad);

        if (images != null && !images.isEmpty()) {
            saveAdImages(saveAd, images);
        }

        return saveAd;
    }

    @Transactional
    public Ad updateAd(Long adId, AdDTO adDTO, User user) {
        Ad ad = adRepository.findById(adId).orElseThrow();

        if (!ad.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Нет прав на редактирование этого объявления");
        }

        ad.setTitle(adDTO.getTitle());
        ad.setDescription(adDTO.getDescription());
        ad.setPrice(adDTO.getPrice());
        ad.setCategory(categoryRepository.findById(adDTO.getCategoryId()).orElseThrow());
        ad.setCity(cityRepository.findById(adDTO.getCityId()).orElseThrow());

        return adRepository.save(ad);
    }

    @Transactional
    public void deleteAd(Long adId, User user) {
        Ad ad = adRepository.findById(adId).orElseThrow();

        if (!ad.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Нет прав на удаление этого объявления");
        }

        adRepository.delete(ad);
    }

    @Transactional(readOnly = true)
    public Page<Ad> searchAds(SearchDTO searchDTO) {
        int page = searchDTO.getPage() != null ? searchDTO.getPage() : 0;
        int size = searchDTO.getSize() != null ? searchDTO.getSize() : 10;

        if (searchDTO.getCategoryId() != null) {
            return adRepository.findByStatusAndCategory(1L, searchDTO.getCategoryId(),
                    PageRequest.of(page, size, Sort.by("createdAt").descending()));
        }

        return adRepository.searchAds(1L, searchDTO.getSearchTerm(),
                PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }

    @Transactional(readOnly = true)
    public Page<Ad> findByCategory(Long categoryId, int page, int size) {
        return adRepository.findByStatusAndCategory(1L, categoryId,
                PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }

    private void saveAdImages(Ad ad, List<MultipartFile> files) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        int order = 0;
        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            AdImage adImage = new AdImage();
            adImage.setAd(ad);
            adImage.setFilePath(filePath.toString());
            adImage.setFileName(fileName);
            adImage.setFileSize(file.getSize());
            adImage.setMimeType(file.getContentType());
            adImage.setDisplayOrder(order++);
            adImage.setIsMain(order == 1);

            ad.getImages().add(adImage);
        }
    }
}