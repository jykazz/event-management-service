/**
 * Controller class for handling user-related requests.
 *
 * <p>
 * This class is responsible for handling requests related to users, such as viewing users, creating users, editing user profiles, and changing passwords.
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
 * @see org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
 * @see org.springframework.security.crypto.password.PasswordEncoder
 * @see org.springframework.ui.Model
 * @see org.springframework.validation.BindingResult
 * @see org.springframework.web.bind.annotation.GetMapping
 * @see org.springframework.web.bind.annotation.ModelAttribute
 * @see org.springframework.web.bind.annotation.PathVariable
 * @see org.springframework.web.bind.annotation.PostMapping
 * @see ru.rsreu.lutikov.sber.domain.Event
 * @see ru.rsreu.lutikov.sber.domain.User
 * @see ru.rsreu.lutikov.sber.services.UserService
 * @see java.security.Principal
 * @see java.util.List
 * @since 2023-07-04
 */

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
import ru.rsreu.lutikov.sber.domain.User;
import ru.rsreu.lutikov.sber.services.UserService;

import java.security.Principal;
import java.util.List;

@Controller
public class NewUserController {

    @Autowired
    UserService userService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Handles requests to the "/users" path.
     *
     * @param model  the Model object for passing data to the view
     * @return the name of the view to be rendered
     */
    @GetMapping("/users")
    public String users(Model model) {
        List<User> yourDataList = userService.getAllUsers(); // Retrieve JSON data
        model.addAttribute("yourDataList", yourDataList); // Pass data to the view model
        return "users"; // Return the name of the view to be rendered
    }

    /**
     * Handles requests to create a new user.
     *
     * @param model  the Model object for passing data to the view
     * @return the name of the view to be rendered
     */
    @GetMapping("/users/new")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        return "newUser";
    }

    /**
     * Handles requests to create a new user.
     *
     * @param user    the User object containing the user data
     * @param result  the BindingResult object for validation result
     * @return the redirect view name after creating the user
     */
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

    /**
     * Handles requests to edit a user.
     *
     * @param userId  the ID of the user to edit
     * @param model   the Model object for passing data to the view
     * @return the name of the view to be rendered
     */
    @GetMapping("/users/{userId}/edit")
    public String editUserForm(@PathVariable("userId") Long userId, Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "editUser";
    }

    /**
     * Handles requests to edit a user.
     *
     * @param userId        the ID of the user to edit
     * @param updatedUser   the User object containing the updated user data
     * @return the redirect

    view name after updating the user
     */
    @PostMapping("/users/{userId}/edit")
    public String updateUser(@PathVariable("userId") Long userId, @ModelAttribute User updatedUser) {
        // Update the user with the specified userId using the UserService
        String password = userService.getUserById(userId).getPassword();
        updatedUser.setPassword(password);
        userService.updateUser(userId, updatedUser);

        return "redirect:/users";
    }

    /**
     * Handles requests to delete a user.
     *
     * @param userId  the ID of the user to delete
     * @param model   the Model object for passing data to the view
     * @return the name of the view to be rendered
     */
    @GetMapping("/users/{userId}/delete")
    public String deleteUserForm(@PathVariable Long userId, Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "deleteUser";
    }

    /**
     * Handles requests to delete a user.
     *
     * @param userId  the ID of the user to delete
     * @return the redirect view name after deleting the user
     */
    @PostMapping("/users/{userId}/delete")
    public String deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);

        return "redirect:/users"; // Redirect to the users page after deletion
    }

    /**
     * Handles requests to view the user profile.
     *
     * @param model       the Model object for passing data to the view
     * @param principal   the Principal object representing the current authenticated user
     * @return the name of the view to be rendered
     */
    @GetMapping("/user/profile")
    public String userProfile(Model model, Principal principal) {
        String username = principal.getName();
        Long userId = userService.getUserIdByUsername(username);
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "userProfile";
    }

    /**
     * Handles requests to edit the user profile.
     *
     * @param model       the Model object for passing data to the view
     * @param principal   the Principal object representing the current authenticated user
     * @return the name of the view to be rendered
     */
    @GetMapping("/user/profile/edit")
    public String editUserProfileForm(Model model, Principal principal) {
        String username = principal.getName();
        Long userId = userService.getUserIdByUsername(username);
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "editUserProfile";
    }

    /**
     * Handles requests to edit the user profile.
     *
     * @param editedUser  the User object containing the edited user data
     * @param principal   the Principal object representing the current authenticated user
     * @return the redirect view name after updating the user profile
     */
    @PostMapping("/user/profile/edit")
    public String editUserProfile(@ModelAttribute("user") User editedUser, Principal principal) {
        String username = principal.getName();
        Long userId = userService.getUserIdByUsername(username);
        User user = userService.getUserById(userId);
        user.setEmail(editedUser.getEmail());
        userService.updateUser(userId, user);
        return "redirect:/user/profile";
    }

    /**
     * Handles requests to change the user password.
     *
     * @param model       the Model object for passing data to the view
     * @param principal   the Principal object representing the current authenticated user
     * @return the name of the view to be rendered
     */
    @GetMapping("/user/profile/password")
    public String changeUserPasswordForm(Model model, Principal principal) {
        String username = principal.getName();
        Long userId = userService.getUserIdByUsername(username);
        User user = userService.getUserById(userId

        );
        model.addAttribute("user", user);
        return "changePassword";
    }

    /**
     * Handles requests to change the user password.
     *
     * @param editedUser  the User object containing the edited user data
     * @param principal   the Principal object representing the current authenticated user
     * @return the redirect view name after changing the password
     */
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
