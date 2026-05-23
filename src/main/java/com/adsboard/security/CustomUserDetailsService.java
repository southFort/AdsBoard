package com.adsboard.security;

import com.adsboard.entity.User;
import com.adsboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * Кастомный сервис для загрузки данных пользователя из БД при аутентификации
 * в Spring Security
 * Реализаует интерфейс UserDetailsService, который используется Spring Security
 * для получения информации о пользователе по его имени
 * Важно: Если поле isEnabled у пользователя равно null, пользователь
 * считается отключенным (disabled)
 * - Загружает пользователя из БД через UserService
 * - Обрабатывает случай, кога пользователь не найден в БД
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    /**
     * Загружает данные пользователя по его имени пользователя (username)
     * Метод вызывает Spring Security по время аутентификации для получения
     * информации о пользователе, включая пароль (для проверки) и роли (для авторизации)
     *
     * Логирование включено на время отладки
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("пользователь не найден: " + username));

        System.out.println("Загружен пользователь: " + user.getUsername());
        System.out.println("Хранимый пароль: " + user.getPassword());
        System.out.println("Пароль закодировал? " + user.getPassword().startsWith("$2a$"));

        boolean enabled = Boolean.TRUE.equals(user.isEnabled());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                enabled,
                true, true, true,
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                        .collect(Collectors.toList())
        );
    }
}