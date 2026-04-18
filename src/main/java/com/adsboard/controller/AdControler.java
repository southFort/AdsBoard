package com.adsboard.controller;

import com.adsboard.dto.AdDTO;
import com.adsboard.entity.Ad;
import com.adsboard.entity.User;
import com.adsboard.service.AdService;
import com.adsboard.service.CategoryService;
import com.adsboard.service.CityService;
import com.adsboard.service.UserService;
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

@Controller
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdControler {

    private final AdService adService;
    private final CategoryService categoryService;
    private final CityService cityService;
    private final UserService userService;

    @GetMapping
    public String listAds(@RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size,
                          Model model) {
        Page<Ad> ads = adService.findAllPublicAds(page, size);
        model.addAttribute("ads", ads);
        model.addAttribute("categories", categoryService.getRootCategories());
        return "ads/list";
    }

    @GetMapping("/{id}")
    public String viewAd(@PathVariable Long id, Model model) {
        Ad ad = adService.findById(id);
        model.addAttribute("ad", ad);
        return "ads/view";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("adDTO", new AdDTO());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("citias", cityService.getAllCities());
        return "ads/create";
    }

    @PostMapping("/create")
    public String createAd(@Valid @ModelAttribute AdDTO adDTO,
                           BindingResult bindingResult,
                           @RequestParam("images")List<MultipartFile> images,
                           @AuthenticationPrincipal UserDetails userDetails,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("cities", cityService.getAllCities());
            return "ads/create";
        }

        try {
            //Получаем пользователя из сервиса
            User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
            adService.creareAd(adDTO, user, images);
            redirectAttributes.addFlashAttribute("success", "Объявление успешно создано!");
            return "redirect:/my_ads";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при создании объявления: " + e.getMessage());
            return "redirect:/ads/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, Principal principal) {
        Ad ad = adService.findById(id);
        //Проверка прав пользователя
        model.addAttribute("adDTO", convertToDTO(ad));
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("cities", cityService.getAllCities());
        return "ads/edit";
    }

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