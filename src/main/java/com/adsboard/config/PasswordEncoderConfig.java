package com.adsboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    /**
     * Конфигурационный класс для настройки кодировщика паролей
     * Определяет бил PasswordEncoder, который используется Spring Security
     * для хеширования паролей пользователей при регистрации и проверки
     * при аутентификации
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
