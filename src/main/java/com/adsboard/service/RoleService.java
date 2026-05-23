package com.adsboard.service;

import com.adsboard.entity.Role;
import com.adsboard.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Set;

/**
 * Сервис для управления ролями пользователей.
 * Предоставляет бизнес-логику для получения ролей.
 */
@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    /**
     * Находит роли по набору названий.
     */
    public Set<Role> findByNameIn(Set<String> names) {
        return roleRepository.findByNameIn(names);
    }
}