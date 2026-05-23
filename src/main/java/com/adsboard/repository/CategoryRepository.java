package com.adsboard.repository;

import com.adsboard.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Репозиторий для работы с сущностью Category.
 * Предоставляет методы для работы с иерархической структорой категорий,
 * включая получение корневых категорий, дочерних и подгрузку связей
 * для избежания проблемы N+1
 * - Корневые категории: parent = null
 * - Дочерние категории: ссылаются на родителя через parent
 * - Поддерживается любая глубина вложенности
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Находит Все корневые категории.
     * Используется для построения верхнего уровня навигации по категориям.
     */
    List<Category> findByParentIsNull();

    /**
     * Находит все дочерние категории указанной родительской категории.
     */
    List<Category> findByParentId(Long parentId);

    /**
     * Находит все категории с подгрузкой дочерних категорий и родителя.
     * Применяется для построения полного дерева категорий или для кэширования
     * всех категорий сразу.
     */
    @Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.children LEFT JOIN FETCH c.parent")
    List<Category> findAllWithChildren();

    /**
     * Находит все корневые категории с подгрузкой дочерних и родителя.
     * Используется для построения навигации по категориям с возможностью
     * отображения дочерних категорий без доп-ных запросов к БД
     */
    @Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.children LEFT JOIN FETCH c.parent WHERE c.parent IS NULL ")
    List<Category> findRootWithChildren();
}