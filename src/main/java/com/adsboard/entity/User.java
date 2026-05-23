package com.adsboard.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Сущность, представляющая пользователя системы
 * Класс отображается на таблицу "users" вБД и содержит всю информацию
 * о пользователе: учетные данные, контактную информацию, роли и объявления
 * - Поддержка роле для авторизации (многие-ко-многим с Role)
 * - Автоматическая установка даты регистрации
 * - Каскадное удаление объявлений пользователя
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    /**
     * Уникальный идентификатор пользователя
     * Генерируется автоматически с использованием последовательности "users_id_seq"
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
    private Long id;

    /**
     * Уникальное имя пользователя (логин)
     * - Обязательное поле не может быть null
     * - Должно быть уникальным в пределах таблицы
     * - Максимальная длина - 50 символов
     */
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /**
     * Хэшированный пароль пользователя
     * - Обязательное поле, не может быть null
     * - Максимальная длина - 255 символов
     */
    @Column(nullable = false, length = 255)
    private String password;

    /**
     * Уникальный email-адрес пользователя
     * - Обязательное поле, не может быть null
     * - Должен быть уникальный в пределах таблицы
     * - Максимальная длина - 100 символов
     */
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /**
     * Номер телефона пользователя
     * - Максимальная длина - 20 символов
     */
    @Column(length = 20)
    private  String phone;

    /**
     * Полное имя пользователя
     * - Максимальная длина - 100 символов
     */
    @Column(name = "full_name", length = 100)
    private  String fullName;

    /**
     * Дата и время регистрации пользователя
     * - Устанавливается автоматически перед сохранением см. onCreate()
     */
    @Column(name = "created_at")
    private LocalDateTime createAt;

    /**
     * Флаг, указывающий, активная ли учетная запись пользователя
     * - Обязательное поле не может быть null
     * - По умолчанию устанавливается true (активна)
     * - Отключенные пользователи не могут авторизоваться в системе
     */
    @Column(name = "is_enabled", nullable = false)
    @Builder.Default
    private boolean isEnabled = true;

    /**
     * Коллекция ролей пользователя для авторизации
     * - Связь многе-ко-многим с сущностью Role
     * - Загружается жадным способом (EAGER), т.к. роли нужны сразу
     * при аутентификации пользователя
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    /**
     * Коллекция объявлений созданных пользователем
     * - Связь один-ко-многим с сущностью Ad
     * - Применяется каскадное удаление (при удалении пользователя удаляются его объявления)
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Ad> ads = new HashSet<>();

    /**
     * JPA-колбэк, выполняется перед сохранением нового пользователя
     * Устанавливает поле createAt текущей датой
     */
    @PrePersist
    protected void onCreate() {
        createAt = LocalDateTime.now();
    }
}
