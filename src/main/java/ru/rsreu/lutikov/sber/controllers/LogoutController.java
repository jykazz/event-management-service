package ru.rsreu.lutikov.sber.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            // Инвалидация сеанса
            session.invalidate();
        }
        // Перенаправление на страницу после выхода
        return "redirect:/login?logout";
    }
}

