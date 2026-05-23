package com.adsboard.controller;

import com.adsboard.dto.AdDTO;
import com.adsboard.entity.Ad;
import com.adsboard.entity.User;
import com.adsboard.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
import java.util.List;

/**
 * Контроллер для управления объявлениями.
 * Обрабатывает HTTP-запросы, связанные с CRUD операциями над объявлениями.
 * Основные маршруты:
 * GET /ads - список всех активных объявлений
 * GET /ads/{id} - детальный просмотр объявления
 * GET /ads/create - форма создания объявления
 * POST /ads/create - обработка создания объявления
 * GET /ads/edit/{id} - форма редактирования объявления
 * POST /ads/edit/{id} - обработка редактирования объявления
 * POST /ads/delete - удаление объявления
 * Требования к аутентификации:
 * - Просмотр объявлений - доступен всем
 * - Создание/редактирование/удаление - только авторизованным
 * - Редактирование/удаление - только владельцу объявления
 */
@Controller
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdController {

    private final AdService adService;
    private final CategoryService categoryService;
    private final RegionService regionService;
    private final CityService cityService;
    private final UserService userService;

    /**
     * Отображает список всех активных объявлений.
     * Статус активного объявления = 11
     */
    @GetMapping
    public String listAds(@RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size,
                          Model model) {
        Page<Ad> ads = adService.findAllPublicAds(page, size);
        model.addAttribute("ads", ads);
        model.addAttribute("categories", categoryService.getRootCategories());
        return "ads/list";
    }

    /**
     * Отображает детальную страницу объявления.
     * При просмотре счетчик просмотров увеличивается
     */
    @GetMapping("/{id}")
    public String viewAd(@PathVariable Long id, Model model) {
        Ad ad = adService.findById(id);
        model.addAttribute("ad", ad);
        return "ads/view";
    }

    /**
     * Отображается форму создания нового объявления
     * Доступно только авторизованным пользователям
     */
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("adDTO", new AdDTO());
        model.addAttribute("categories", categoryService.getAllCategoriesDTO());
        model.addAttribute("regions", regionService.getAllRegions());
        return "ads/create";
    }

    /**
     * Обработка создания нового объявления.
     */
    @PostMapping("/create")
    public String createAd(@Valid @ModelAttribute AdDTO adDTO,
                           BindingResult bindingResult,
                           @RequestParam("images")List<MultipartFile> images,
                           @AuthenticationPrincipal UserDetails userDetails,
                           RedirectAttributes redirectAttributes,
                           Model model) {

        //Логируем полученные данные - временно пока дебажим
        System.out.println("===START createAd===");
        System.out.println("Username: " + (userDetails != null ? userDetails.getUsername() : "null"));
        System.out.println("Title: " + adDTO.getTitle());
        System.out.println("Category ID: " + adDTO.getCategoryId());
        System.out.println("City ID type: " + adDTO.getCityId().getClass().getName());
        System.out.println("City ID: " + adDTO.getCityId());
        System.out.println("Images count: " + (images != null ? images.size() : 0));

        if (adDTO.getCityId() == null) {
            bindingResult.rejectValue("cityId", "error.cityId", "Выберите город");
        }

        if (bindingResult.hasErrors()) {
            System.out.println("BindingResult has errors!");
            model.addAttribute("categories", categoryService.getAllCategoriesDTO());
            model.addAttribute("regions", regionService.getAllRegions());
            return "ads/create";
        }

        try {
            //Получаем пользователя из сервиса
            User user = userService.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + userDetails.getUsername()));

            System.out.println("User found: " + user.getUsername() + ", ID: " + user.getId());

            adService.createAd(adDTO, user, images);

            redirectAttributes.addFlashAttribute("success", "Объявление успешно создано!");
            return "redirect:/my-ads";
        } catch (Exception e) {
            System.out.println("ERROR in creareAd: " + e.getMessage());
            e.printStackTrace();

            redirectAttributes.addFlashAttribute("error", "Ошибка при создании объявления: " + e.getMessage());
            return "redirect:/ads/create";
        }
    }

    /**
     * Отображает форму редактирования объявления.
     * Доступно только владельцу объявления
     */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, Principal principal) {
        Ad ad = adService.findById(id);
        model.addAttribute("adDTO", convertToDTO(ad));
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("cities", cityService.getAllCities());
        return "ads/edit";
    }

    /**
     * Обрабатывает обновление объявления.
     * Перед обновление проверяет, что текущий пользователь является владельцем.
     */
    @PostMapping("/edit/{id}")
    public String updateAd(@PathVariable Long id,
                           @Valid @ModelAttribute AdDTO adDTO,
                           BindingResult bindingResult,
                           @AuthenticationPrincipal UserDetails userDetails,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "ads/edit";
        }

        try {
            User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
            adService.updateAd(id, adDTO, user);
            redirectAttributes.addFlashAttribute("success", "Объявление успешно обновлено!");
            return "redirect:/my-ads";
        } catch (SecurityException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/my-ads";
        }
    }

    /**
     * Удаляет объявление.
     */
    @PostMapping("/delete/{id}")
    public String deleteAd(@PathVariable Long id,
                           @AuthenticationPrincipal UserDetails userDetails,
                           RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
            adService.deleteAd(id, user);
            redirectAttributes.addFlashAttribute("success", "Объявление успешно удалено!");
        } catch (SecurityException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/my-ads";
    }

    /**
     * Конвертируется сущность Ad в AdDTO
     */
    private AdDTO convertToDTO(Ad ad) {
        return AdDTO.builder()
                .id(ad.getId())
                .title(ad.getTitle())
                .description(ad.getDescription())
                .price(ad.getPrice())
                .categoryId(ad.getCategory().getId())
                .cityId(ad.getCity().getId())
                .statusId(ad.getStatus().getId())
                .build();
    }
}