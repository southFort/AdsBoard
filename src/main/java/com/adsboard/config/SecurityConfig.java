package com.adsboard.config;

import com.adsboard.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Основная конфигурация безопасности Spring Security
 * Класс настраивает аутентификацию, авторизацию, защиту от CSRF,
 * форму входа, выхода из системы и правила доступа к URL-адресам
 * - Включена поддержка аннотаций безопасности на уровне методов (@EnableMethodSecurity)
 * - Настроена кастомная форма логина вместо стандартной spring Security
 * - Определены публичный и защищенные маршруты
 * - Используется кастомный сервис для загрузки пользователей, см. CustomUserDetailsService
 * - CSRF защита на момент разработки закомментирована
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;


    /**
     * Создает и настраивает бин AuthenticationManager, для обработки
     * запросов аутентификации
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Настраивает цепочку фильтров безопасности для HTTP-запросов
     * Определяет правила авторизации, настройки формы входа,
     * обработку выхода из системы и сервис загрузки пользователей
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/static/**", "/css/**", "/js/**", "/images/**", "/uploads/**").permitAll()
                        .requestMatchers("/", "/ads/**", "/categories/**", "/cities/**", "/search").permitAll()
                        .requestMatchers("/register", "/login").permitAll()
                        .requestMatchers("/my-ads/**", "/ads/create", "/ads/edit/**", "/ads/delete/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/my-ads",true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/?logout=true")
                        .permitAll()
                )
                .userDetailsService(userDetailsService);

        return http.build();
    }
}