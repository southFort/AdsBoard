package com.adsboard.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Сущность, представляющая категорию объявлений с поддержкой древовидной иерархии
 * Класс отображается на таблицу "categories" в БД и позволяет организовать
 * категории в виде дерева (родительские и дочерние
 * - Поддержка вложенных категорий (parent/children)
 * - Каскадное удаление дочерних категорий при удалении родительской
 * - Ленивая загрузка связных сущностей для оптимизации
 */
@Entity
@Table(name = "categories")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    /**
     * Уникальный идентификатор категории
     * Генерируется автоматически с использованием последовательностей "categories_id_seq"
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categories_id_seq")
    @SequenceGenerator(name = "categories_id_seq", sequenceName = "categories_id_seq", allocationSize = 1)
    private Long id;

    /**
     * Название категории
     * - Обязательное поле, не может быть null
     * - Максимальная длина - 100 символов
     */
    @Column(nullable = false, length =100)
    private String name;

    /**
     * Родительская категория
     * - Связь многие-к-одному с сущностью Category
     * - Загружается лениво для оптимизации производительности
     * - Если null, категория является родительской
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    /**
     * Коллекция дочерних категорий
     * - Связь один-ко-многим с сущностью Category
     * - Исключено из toString и equals/hashCode для предотвращения рекурсии
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Category> children = new HashSet<>();

    /**
     * Коллекция объявлений, относящихся к данной категории
     * - Связь один-ко-многим с сущностью Ad
     * - Исключено из toString и equals/hashCode для предотвращения рекурсии
     */
    @OneToMany(mappedBy = "category")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Ad> ads = new HashSet<>();
}