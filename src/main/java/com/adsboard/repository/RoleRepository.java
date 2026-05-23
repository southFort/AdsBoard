package com.adsboard.repository;

import com.adsboard.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Set;

/**
 * Репозиторий для работы с сущностью Role.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Находит все роли, название которых входит в указанный набор.
     * Используется при регистрации пользователя.
     */
    Set<Role> findByNameIn(Set<String> names);
}