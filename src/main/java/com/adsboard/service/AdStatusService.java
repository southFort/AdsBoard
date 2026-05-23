package com.adsboard.service;

import com.adsboard.entity.AdStatus;
import com.adsboard.repository.AdStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис для работы со статусами объявлений.
 */
@Service
@RequiredArgsConstructor
public class AdStatusService {

    private final AdStatusRepository adStatusRepository;

    /**
     * Находит статус по его ID
     */
    @Transactional(readOnly = true)
    public AdStatus findById(Long id) {
        return adStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Status not found with id: " + id));
    }
}
