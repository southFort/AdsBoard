package com.adsboard.controller;

import com.adsboard.entity.Ad;
import com.adsboard.entity.Category;
import com.adsboard.dto.CategoryWithAdsCountDTO;
import com.adsboard.service.AdService;
import com.adsboard.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Контроллер для управления категориями объявлений.
 * Обрабатывает запросы, связанные с просмотром категорий и их иерархии
 * Основные маршруты
 * GET /categories - список всех категорий с количеством объявлений
 * GET /categories/{id} - просмотр категории с объявлениями и подкатегориями
 */

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final AdService adService;

    /**
     * Отображает список всех корневых категорий
     */
    @GetMapping
    public String listCategories(Model model) {
        List<CategoryWithAdsCountDTO> categories = categoryService.getRootCategoriesWithCount();
        model.addAttribute("rootCategories", categories);
        //model.addAttribute("allCategories", categoryService.getAllCategoriesDTO());
        return "categories/list";
    }

    /**
     * Отображает детальную страницу категории: список дочерних категорий и
     * список объявлений в текущей категории
     */
    @GetMapping("/{id}")
    public String viewCategory (@PathVariable Long id,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                Model model) {
        Category category = categoryService.findById(id);
        Page<Ad> ads = adService.findByCategory(id, page, size);
        List<CategoryWithAdsCountDTO> subcategories = categoryService.getChildren(id).stream()
                .map(cat -> {
                    CategoryWithAdsCountDTO dto = new CategoryWithAdsCountDTO();
                    dto.setId(cat.getId());
                    dto.setName(cat.getName());
                    dto.setParentId(cat.getParent() != null ? cat.getParent().getId() : null);
                    return dto;
                })
                .toList();

        model.addAttribute("category", category);
        model.addAttribute("ads", ads);
        model.addAttribute("subcategories", subcategories);
        model.addAttribute("currentCategoryId", id);

        return "categories/view";
    }
}
