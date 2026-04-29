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
 * getRootCategories() - получить все корневые категории
 * getAllCategories() - получить все категории
 * findById(id) - найти категорию по id
 * getRootCategoriesWithCount() - получить DTO корневых категорий с кол-вом объявлений
 * getAllCategoriesDTO() - получить DTO всех категорий
 * convertToDTO(Category category) - конвертация Entity > DTO
 * convertToCountDTO(Category category) - конвертация Entity > CountDTO
 * getAdsCount(Long categoryId) - получить кол-во объявлений в категории
 */

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AdRepository adRepository;

    @Transactional(readOnly = true)
    public List<Category> getRootCategories() {
        return categoryRepository.findByParentIsNull();
    }

    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAllWithChildren();
    }

    @Transactional(readOnly = true)
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Категория не найдена: " + id));
    }

    @Transactional(readOnly = true)
    public List<Category> getChildren(Long parentId) {
        return categoryRepository.findByParentId(parentId);
    }

    @Transactional(readOnly = true)
    public List<CategoryWithAdsCountDTO> getRootCategoriesWithCount() {
        Map<Long, Integer> adsCounts = loadAdsCountForAllCategories();

        List<Category> rootCategories = categoryRepository.findRootWithChildren();
        return rootCategories.stream()
                .map(category -> convertToCountDTO(category, adsCounts))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategoriesDTO() {
        Map<Long, Integer> adsCounts = loadAdsCountForAllCategories();

        List<Category> categories = categoryRepository.findAllWithChildren();

        return categories.stream()
                .map(category -> convertToDTO(category, adsCounts))
                .collect(Collectors.toList());
    }

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

    private CategoryWithAdsCountDTO convertToCountDTO(Category category, Map<Long, Integer> adsCounts) {

        return CategoryWithAdsCountDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .adsCount(adsCounts.getOrDefault(category.getId(), 0))
                .build();
    }

    private Map<Long, Integer> loadAdsCountForAllCategories() {
        return adRepository.countAdsGroupedByCategory().stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> ((Long) row[1]).intValue()
                ));
    }
}