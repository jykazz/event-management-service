package ru.rsreu.lutikov.sber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.rsreu.lutikov.sber.domain.Event;
import ru.rsreu.lutikov.sber.domain.User;
import ru.rsreu.lutikov.sber.services.UserService;

import java.security.Principal;
import java.util.List;

@Controller
public class NewUserController {

    @Autowired
    UserService userService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/users")
    public String events(Model model) {
        List<User> yourDataList = userService.getAllUsers(); // Получение данных JSON
        model.addAttribute("yourDataList", yourDataList); // Передача данных в модель представления
        return "users"; // Возвращаем имя представления
    }

    @GetMapping("/users/new")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        return "newUser";
    }

    @PostMapping("/users/new")
    public String createUser(@ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "newUser";
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userService.createUser(user);
        return "redirect:/users";
    }

    @GetMapping("/users/{userId}/edit")
    public String editUserForm(@PathVariable("userId") Long userId, Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "editUser";
    }

    @PostMapping("/users/{userId}/edit")
    public String updateUser(@PathVariable("userId") Long userId, @ModelAttribute User updatedUser) {
        // Обновление пользователя с указанным userId с помощью сервиса UserService
        String password = userService.getUserById(userId).getPassword();
        updatedUser.setPassword(password);
        userService.updateUser(userId, updatedUser);

        return "redirect:/users";
    }

    @GetMapping("/users/{userId}/delete")
    public String deleteUserForm(@PathVariable Long userId, Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "deleteUser";
    }

    @PostMapping("/users/{userId}/delete")
    public String deleteEventForm(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);

        return "redirect:/users"; // Перенаправление на страницу с пользователями после удаления
    }

    @GetMapping("/user/profile")
    public String userProfile(Model model, Principal principal) {
        String username = principal.getName();
        Long userId = userService.getUserIdByUsername(username);
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "userProfile";
    }

    @GetMapping("/user/profile/edit")
    public String editUserProfileForm(Model model, Principal principal) {
        String username = principal.getName();
        Long userId = userService.getUserIdByUsername(username);
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "editUserProfile";
    }

    @PostMapping("/user/profile/edit")
    public String editUserProfile(@ModelAttribute("user") User editedUser, Principal principal) {
        String username = principal.getName();
        Long userId = userService.getUserIdByUsername(username);
        User user = userService.getUserById(userId);
        user.setEmail(editedUser.getEmail());
        userService.updateUser(userId, user);
        return "redirect:/user/profile";
    }

    @GetMapping("/user/profile/password")
    public String changeUserPasswordForm(Model model, Principal principal) {
        String username = principal.getName();
        Long userId = userService.getUserIdByUsername(username);
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "changePassword";
    }

    @PostMapping("/user/profile/password")
    public String changePassword(@ModelAttribute("user") User editedUser, Principal principal) {
        String username = principal.getName();
        Long userId = userService.getUserIdByUsername(username);
        User user = userService.getUserById(userId);

        String encodedPassword = passwordEncoder.encode(editedUser.getPassword());
        user.setPassword(encodedPassword);
        userService.updateUser(userId, user);

        return "redirect:/user/profile";
    }


}
