package com.adsboard.controller;

import com.adsboard.service.AdService;
import com.adsboard.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Главный контроллер приложения.
 * Обрабатывает запросы к главное страницы сайта.
 * Основные маршруты
 * - GET / - главная страница
 */
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final AdService adService;
    private final CategoryService categoryService;

    /**
     * Отображает главную страницу сайта
     * Показывает:
     *  * - Список корневых категорий для быстрйо навигации
     *  * - 8 последних активных объявлений
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("latestAds", adService.findAllPublicAds(0, 8));
        model.addAttribute("categories", categoryService.getRootCategories());
        return "index";
    }
}
