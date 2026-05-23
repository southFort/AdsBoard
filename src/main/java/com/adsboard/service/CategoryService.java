package com.adsboard.service;

import com.adsboard.dto.CategoryDTO;
import com.adsboard.dto.CategoryWithAdsCountDTO;
import com.adsboard.entity.Category;
import com.adsboard.repository.AdRepository;
import com.adsboard.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Сервис для управления категориями объявлений.
 * Предоставляет бизнес-логику для работы с иерархической структурой категорий,
 * включая получение корневых и дочерних категорий, формирование DTO с количеством объявлений.
 */

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AdRepository adRepository;

    /**
     * Получает все корневые категории
     */
    @Transactional(readOnly = true)
    public List<Category> getRootCategories() {
        return categoryRepository.findByParentIsNull();
    }

    /**
     * Получает все категории с подгрузкой корневых и дочерних элементов
     */
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAllWithChildren();
    }

    /**
     * Находит категорию по ID
     */
    @Transactional(readOnly = true)
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Категория не найдена: " + id));
    }

    /**
     * Получает дочерние категории по родительской
     */
    @Transactional(readOnly = true)
    public List<Category> getChildren(Long parentId) {
        return categoryRepository.findByParentId(parentId);
    }

    /**
     * Получает корневые категории с количеством объявлений в каждом.
     * Возвращается DTO, содержащее информацию о категории и
     * количество объявлений в нем.
     */
    @Transactional(readOnly = true)
    public List<CategoryWithAdsCountDTO> getRootCategoriesWithCount() {
        Map<Long, Integer> adsCounts = loadAdsCountForAllCategories();

        List<Category> rootCategories = categoryRepository.findRootWithChildren();
        return rootCategories.stream()
                .map(category -> convertToCountDTO(category, adsCounts))
                .collect(Collectors.toList());
    }

    /**
     * Получает все категории в виде DTO с рекурсивной структурой.
     * Возвращает полное дерево категорий, где каждая категория содержит
     * список своих дочерних категорий (также в виде DTO)
     */
    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategoriesDTO() {
        Map<Long, Integer> adsCounts = loadAdsCountForAllCategories();

        List<Category> categories = categoryRepository.findAllWithChildren();

        return categories.stream()
                .map(category -> convertToDTO(category, adsCounts))
                .collect(Collectors.toList());
    }

    /**
     * Конвертирует сущность Category в CategoryDTO.
     */
    private CategoryDTO convertToDTO(Category category, Map<Long, Integer> adsCounts) {

        List<Category> childrenCopy = new ArrayList<>(category.getChildren());

        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .parentName(category.getParent() != null ? category.getParent().getName() : null)
                .children(childrenCopy.stream()
                        .map(child -> convertToDTO(child, adsCounts))
                        .collect(Collectors.toList()))
                .adsCount(adsCounts.getOrDefault(category.getId(), 0))
                .build();
    }

    /**
     * Конвертирует сущность Category в CategoryWithAdsCountDTO.
     * Упрощенная версия DTO без рекурсивной структуры children
     */
    private CategoryWithAdsCountDTO convertToCountDTO(Category category, Map<Long, Integer> adsCounts) {

        return CategoryWithAdsCountDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .adsCount(adsCounts.getOrDefault(category.getId(), 0))
                .build();
    }

    /**
     * Загружает количество объявлений для всех категорий.
     * Выполняет один запрос к БД и возвращается мапу, где
     * ключ - ID категории, значение - кол-во объявлений.
     */
    private Map<Long, Integer> loadAdsCountForAllCategories() {
        return adRepository.countAdsGroupedByCategory().stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> ((Long) row[1]).intValue()
                ));
    }
}