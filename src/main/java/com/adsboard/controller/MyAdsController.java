package com.adsboard.controller;

import com.adsboard.entity.Ad;
import com.adsboard.entity.User;
import com.adsboard.service.AdService;
import com.adsboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/my-ads")
@RequiredArgsConstructor
public class MyAdsController {

    private final AdService adService;
    private final UserService userService;

    @GetMapping
    public String myAds(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @AuthenticationPrincipal UserDetails userDetails,
                        Model model) {
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        Page<Ad> ads = adService.findUserAds(user.getId(), page, size);

        model.addAttribute("ads", ads);
        model.addAttribute("user", user);
        return "my-ads/list";
    }

}
