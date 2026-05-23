package com.adsboard.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Сущность, представляющая роль пользователя в системе
 * Класс отображается на таблицу "roles" в БД и используется
 * для управления доступом и авторизации
 * Типичные роли:
 * ROLE_USER - обычный пользователь (роль по умолчанию)
 * ROLE_ADMIN - администратор системы
 * ROLE_MODERATOR - модератор объявлений
 */
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    /**
     * Уникальный идентификатор роли
     * Ганарируется автоматически с использованием последовательности "roles_ia_seq"
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_id_seq")
    @SequenceGenerator(name = "roles_id_seq", sequenceName = "roles_id_seq", allocationSize = 1)
    private Long id;

    /**
     * Уникальное название роли
     * - Обязательное поле, не может быть null
     * - Уникальное в пределах таблицы
     * - Максимальная длина - 50 символов
     * - В названиях используем префикс "ROLE_" для совместимости со Spring Security
     */
    @Column(nullable = false, unique = true, length = 50)
    private String name;
}
