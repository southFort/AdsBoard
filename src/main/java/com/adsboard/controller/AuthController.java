package com.adsboard.controller;

import ch.qos.logback.classic.Logger;
import com.adsboard.entity.User;
import com.adsboard.dto.UserDTO;
import com.adsboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute UserDTO userDTO,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        logger.info("Получен UserDTO: {}", userDTO);

        if (bindingResult.hasErrors()) {
            logger.warn("Ошибки валидации: {}", bindingResult.getAllErrors());
            return "auth/register";
        }

        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error", "Пароли не совпадают");
            return "auth/register";
        }

        if (userService.existsByUsername(userDTO.getUserName())) {
            bindingResult.rejectValue("username", "error", "Имя пользователя уже занято");
            return "auth/register";
        }

        if (userService.existsByEmail(userDTO.getEmail())) {
            bindingResult.rejectValue("email", "error", "Email уже зарегистрирован");
            return "auth/register";
        }

        try {
            User user = User.builder()
                    .username(userDTO.getUserName())
                    .email(userDTO.getEmail())
                    .password(userDTO.getPassword())
                    .phone(userDTO.getPhone())
                    .fullName(userDTO.getFullName())
                    .build();

            userService.createUser(user, Set.of("USER"));
            redirectAttributes.addFlashAttribute("success", "Регистрация успешна! Теперь вы можете войти.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка регистрации: " + e.getMessage());
            return "redirect:/register";
        }
    }
}