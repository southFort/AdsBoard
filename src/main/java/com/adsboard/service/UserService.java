package com.adsboard.service;

import com.adsboard.entity.User;
import com.adsboard.entity.Role;
import com.adsboard.repository.UserRepository;
import com.adsboard.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.Set;

/**
 * Сервис для управления пользователями.
 * Предоставляет бизнес-логику для операций с пользователями:
 * создание, поиск, обновление, проверка существования
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Создает нового пользователя с указанными ролями
     */
    @Transactional
    public User createUser (User user, Set<String> roleNames) {
        // на время тестирования
        // user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = roleRepository.findByNameIn(roleNames);
        user.setRoles(roles);

        return userRepository.save(user);
    }

    /**
     * Находит пользователя по имени пользователя с подгрузкой ролей
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsernameWithRoles(username);
    }

    /**
     * Находит пользователя по идентификатору с подгрузкой ролей
     */
    public Optional<User> findById(Long id) {
        return userRepository.findByIdWithRoles(id);
    }

    /**
     * Проверяет, существует ли пользователь с указанным именем.
     * Используется при регистрации для валидации уникальности username
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Проверяет, существует ли пользователь с указанным emain.
     * Используется при регистрации для валидации уникальности email
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Обновляет данные существующего пользователя
     */
    @Transactional
    public User updateUser (User user) {
        return userRepository.save(user);
    }
}