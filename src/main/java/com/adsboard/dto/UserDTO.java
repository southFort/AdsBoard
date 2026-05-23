package com.adsboard.dto;

import lombok.*;
import jakarta.validation.constraints.*;

/**
 * DTO для передачи данных о пользователе.
 * Используется:
 * - Регистрации новых пользователей
 * - Редактирования профиля пользователя
 * - Отображения информации о пользователе в представлениях
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    @NotBlank(message = "Имя пользователя обязательно")
    @Size(min = 3, max = 50, message = "Имя пользователя должно быть от 3 до 50 символов")
    private String userName;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный email")
    private String email;

    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    private String password;

    private String confirmPassword;

    private String phone;
    private String fullName;
}