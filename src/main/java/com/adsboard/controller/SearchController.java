package com.adsboard.controller;

import com.adsboard.entity.Ad;
import com.adsboard.dto.SearchDTO;
import com.adsboard.service.AdService;
import com.adsboard.service.CategoryService;
import com.adsboard.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для поиска объявлений.
 * Основные маршруты:
 * - GET /search - поиск объявлений
 */
@Controller
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final AdService adService;
    private final CategoryService categoryService;
    private final CityService cityService;

    /**
     * Выполняет поиск объявлений по заданным критериям.
     * Логика:
     * - Формирует SeatchDTO из параметров запроса
     * - выполняет поиск через AdService
     * - Передает в представление результаты поиска и фильтры
     */
    @GetMapping
    public String search(@RequestParam(required = false) String q,
                         @RequestParam(required = false) Long category,
                         @RequestParam(required = false) Long city,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size,
                         Model model) {
        SearchDTO searchDTO = SearchDTO.builder()
                .searchTerm(q)
                .categoryId(category)
                .cityId(city)
                .page(page)
                .size(size)
                .build();

        Page<Ad> ads = adService.searchAds(searchDTO);

        model.addAttribute("ads", ads);
        model.addAttribute("searchDTO", searchDTO);
        model.addAttribute("categories", categoryService.getRootCategories());
        model.addAttribute("cities", cityService.getAllCities());

        return "search/results";
    }
}