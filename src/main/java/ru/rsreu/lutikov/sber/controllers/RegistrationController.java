/**
 * Controller class for handling user registration.
 *
 * <p>
 * This class is responsible for handling requests related to user registration, such as displaying the registration form
 * and adding a new user to the system.
 * </p>
 *
 * <p>
 * <strong>Author:</strong> Vadim
 * <br>
 * <strong>Email:</strong> blinvadik@mail.ru
 * </p>
 *
 * @see org.springframework.stereotype.Controller
 * @see org.springframework.beans.factory.annotation.Autowired
 * @see org.springframework.web.bind.annotation.GetMapping
 * @see org.springframework.web.bind.annotation.PostMapping
 * @see ru.rsreu.lutikov.sber.domain.Role
 * @see ru.rsreu.lutikov.sber.domain.User
 * @see ru.rsreu.lutikov.sber.repositories.UserRepository
 * @see org.springframework.security.crypto.password.PasswordEncoder
 * @see org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
 * @since 2023
 */

package ru.rsreu.lutikov.sber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.rsreu.lutikov.sber.domain.Role;
import ru.rsreu.lutikov.sber.domain.User;
import ru.rsreu.lutikov.sber.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Handles requests to the "/registration" path.
     *
     * @return the name of the view to be rendered
     */
    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    /**
     * Handles requests to add a new user.
     *
     * @param user  the User object representing the new user
     * @param model  the Map object for passing data to the view
     * @return the redirect view name after adding the user
     */
    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {
        User userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb != null) {
            model.put("message", "User exists!");
            return "registration";
        }

        user.setActive(true);
        user.setRole(Collections.singleton(Role.USER));
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        return "redirect:/login";
    }
}

