package com.adsboard.repository;

import com.adsboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностью User.
 * Предоставляет методы для поиска пользователей по различным критериям,
 * проверки на существования, а так же загрузки пользователей в их ролями.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Проверка существования пользователя с указанием username.
     * Используется при регистрации дял проверки уникальности имени пользователя.
     */
    boolean existsByUsername(String username);

    /**
     * Проверка существования пользователя с указанием email.
     * Используется при регистрации дял проверки уникальности email.
     */
    boolean existsByEmail (String email);

    /**
     * Находит пользователя по ID с подгрузкой его ролей.
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.id = :id")
    Optional<User> findByIdWithRoles(Long id);

    /**
     * Находит пользователя по username с подгрузкой его ролей.
     * Используется в процессе аутентификации.
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = :username")
    Optional<User> findByUsernameWithRoles(String username);
}