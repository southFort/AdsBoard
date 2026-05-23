package com.adsboard.controller;

import ch.qos.logback.classic.Logger;
import com.adsboard.entity.User;
import com.adsboard.dto.UserDTO;
import com.adsboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import java.util.Set;

/**
 * Контроллер для аутентификации и регистрации пользователей.
 * Обрабатывает запросы, связанные с входом в систему и регистрацией
 * новых пользователей.
 * Основные маршруты:
 * GET /login - страница входа в систему
 * GET /register - страница регистрации нового пользователя
 * POST /register - обработка регистрации
 * Валидация регистрации:
 * - Проверка совпадения пароля и подтверждения
 * - Проверка уникальности username
 * - Проверка уникальности email
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthController.class);

    /**
     * Отображается страницу входа в систему.
     */
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    /**
     * Отображает форму регистрации нового пользователя.
     */
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "auth/register";
    }

    /**
     * Обрабатывает регистрацию нового пользователя
     * Процесс:
     * - Валидация полей
     * - Проверка совпадения пароля и подтверждения
     * - Уникальность имени пользователя и почты
     * - Кодирование пароля
     * - Создание пользователя с ролью USER
     * - перенаправление на страницу логина с сообщением об успехе
     */
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
            String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

            User user = User.builder()
                    .username(userDTO.getUserName())
                    .email(userDTO.getEmail())
                    .password(encodedPassword)
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