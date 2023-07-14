package ru.rsreu.lutikov.sber.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.rsreu.lutikov.sber.domain.User;
import ru.rsreu.lutikov.sber.repositories.UserRepository;
import ru.rsreu.lutikov.sber.services.UserDetailsServiceImpl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String authUser(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           HttpServletRequest request,
                           HttpServletResponse response,
                           Model model) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        username,
                        password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Установка токена в cookies
        Cookie tokenCookie = new Cookie("jwt", jwt);
        tokenCookie.setMaxAge(24 * 60 * 60); // Время жизни токена в секундах (здесь 24 часа)
        tokenCookie.setPath("/");
        response.addCookie(tokenCookie);

        User userDetails = userRepository.findByUsername(username);
        System.out.println(jwt);
        return "redirect:/";
    }
}
