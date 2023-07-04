/**
 * Controller class for handling home-related requests.
 *
 * <p>
 * This class is responsible for handling requests to the root path ("/").
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
 *
 * @since 2023
 */
package ru.rsreu.lutikov.sber.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * Handles requests to the root path ("/").
     *
     * @return the name of the view to be rendered
     */
    @GetMapping("/")
    public String home() {
        return "home.html";
    }
}