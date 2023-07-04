/**
 * Controller class for handling logout-related requests.
 *
 * <p>
 * This class is responsible for handling requests to the "/logout" path and performing logout functionality.
 * </p>
 *
 * <p>
 * <strong>Author:</strong> Vadim
 * <br>
 * <strong>Email:</strong> blinvadik@mail.ru
 * </p>
 *
 * @see org.springframework.stereotype.Controller
 * @see org.springframework.web.bind.annotation.GetMapping
 * @see javax.servlet.http.HttpServletRequest
 * @see javax.servlet.http.HttpSession
 *
 * @since 2023
 */
package ru.rsreu.lutikov.sber.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LogoutController {

    /**
     * Handles requests to the "/logout" path.
     *
     * @param request the HttpServletRequest object to access the current session
     * @return the redirect view name after logout
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            // Invalidating the session
            session.invalidate();
        }

        // Redirecting to the page after logout
        return "redirect:/login?logout";
    }
}