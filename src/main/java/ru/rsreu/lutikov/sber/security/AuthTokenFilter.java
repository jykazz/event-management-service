//package ru.rsreu.lutikov.sber.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//import ru.rsreu.lutikov.sber.services.UserDetailsServiceImpl;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class AuthTokenFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JwtUtils jwtUtils;
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//        try {
//            String jwt = parseJwt(request);
//            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
//                String username = jwtUtils.getUserNameFromJwtToken(jwt);
//                String userRole = jwtUtils.getUserRoleFromJwtToken(jwt);
//
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                UsernamePasswordAuthenticationToken authenticationToken =
//                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                // Добавление роли пользователя в аутентификацию
//                authenticationToken.setDetails(userRole);
//
//                String tokenHeader = "Bearer " + jwt;
//                response.addHeader("Authorization", tokenHeader);
//
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//            } else if (jwtUtils.isTokenExpired(jwt)) {
//                // Выполнение выхода из сессии
//                SecurityContextHolder.clearContext();
//                // Очистка JSESSIONID из кук
//                Cookie[] cookies = request.getCookies();
//                if (cookies != null) {
//                    for (Cookie cookie : cookies) {
//                        if (cookie.getName().equals("JSESSIONID")) {
//                            cookie.setMaxAge(0);
//                            cookie.setPath("/");
//                            response.addCookie(cookie);
//                            break;
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            System.err.println(e);
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private String parseJwt(HttpServletRequest request) {
//        String headerAuth = request.getHeader("Authorization");
//        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
//            return headerAuth.substring(7);
//        }
//        return null;
//    }
//}
package ru.rsreu.lutikov.sber.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.rsreu.lutikov.sber.domain.RefreshToken;
import ru.rsreu.lutikov.sber.domain.User;
import ru.rsreu.lutikov.sber.repositories.RefreshTokenRepository;
import ru.rsreu.lutikov.sber.repositories.UserRepository;
import ru.rsreu.lutikov.sber.services.RefreshTokenService;
import ru.rsreu.lutikov.sber.services.UserDetailsServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SignatureException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwtFromCookies(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                String userRole = jwtUtils.getUserRoleFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Добавление роли пользователя в аутентификацию
                authenticationToken.setDetails(userRole);

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
//            else if (jwtUtils.isTokenExpired(jwt)) {
//                String refreshTokenValue = extractRefreshTokenFromCookie(request);
//                if (refreshTokenValue != null) {
//                    RefreshToken refreshToken = refreshTokenService.findByTokenValue(refreshTokenValue);
//                    if (refreshToken != null) {
////                        refreshTokenService.deleteByTokenValue(refreshTokenValue);
//                        // Проверка действительности refresh-токена
//                        if ((jwtUtils.validateJwtToken(refreshTokenValue)) && (jwtUtils.isTokenExpired(refreshTokenValue))) {
//                            String username = jwtUtils.getUserNameFromJwtToken(refreshTokenValue);
//                            String userRole = jwtUtils.getUserRoleFromJwtToken(refreshTokenValue);
//
//                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                            UsernamePasswordAuthenticationToken authenticationToken =
//                                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                            // Добавление роли пользователя в аутентификацию
//                            authenticationToken.setDetails(userRole);
//
//                            // Создание новой пары access-токена и refresh-токена
//                            String newJwt = jwtUtils.generateJwtToken(authenticationToken);
//                            String newRefreshToken = jwtUtils.generateRefreshToken(userDetails.getUsername());
//
//                            // Сохранение нового refresh-токена в базе данных
//                            refreshToken.setTokenValue(newRefreshToken);
//                            refreshTokenService.save(refreshToken);
//
//                            Cookie tokenCookie = new Cookie("jwt", newJwt);
//                            tokenCookie.setMaxAge(24 * 60 * 60); // Время жизни токена в секундах (здесь 24 часа)
//                            tokenCookie.setPath("/");
//                            response.addCookie(tokenCookie);
//
//                            // Установка нового refresh-токена в куку
//                            Cookie refreshTokenCookie = new Cookie("refreshToken", newRefreshToken);
//                            refreshTokenCookie.setMaxAge(24 * 60 * 60); // Время жизни токена в секундах (здесь 24 часа)
//                            refreshTokenCookie.setPath("/");
//                            response.addCookie(refreshTokenCookie);
//
//                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                        } else {
//                            SecurityContextHolder.clearContext();
//                        }
//                    } else {
//                        SecurityContextHolder.clearContext();
//                    }
//                }
//            }

            else if (jwt == null) {
                SecurityContextHolder.clearContext();
            } else if (!jwtUtils.isValidToken(jwt)) {
                SecurityContextHolder.clearContext();
            }

        } catch (Exception e) {
            String jwt = parseJwtFromCookies(request);
            if (jwtUtils.isTokenExpired(jwt)) {
                String refreshTokenValue = extractRefreshTokenFromCookie(request);
                if (refreshTokenValue != null) {
                    RefreshToken refreshToken = refreshTokenService.findByTokenValue(refreshTokenValue);
                    if (refreshToken != null) {
                        // Проверка действительности refresh-токена
                        if ((jwtUtils.validateJwtToken(refreshTokenValue)) && !(jwtUtils.isTokenExpired(refreshTokenValue))) {
                            String username = jwtUtils.getUserNameFromJwtToken(refreshTokenValue);
                            String userRole = jwtUtils.getUserRoleFromJwtToken(refreshTokenValue);

//                            refreshTokenRepository.deleteByTokenValue(refreshTokenValue);

                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                            UsernamePasswordAuthenticationToken authenticationToken =
                                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                            // Добавление роли пользователя в аутентификацию
                            authenticationToken.setDetails(userRole);

                            // Создание новой пары access-токена и refresh-токена
                            String newJwt = jwtUtils.generateJwtToken(authenticationToken);
                            String newRefreshTokenValue = jwtUtils.generateRefreshToken(userDetails.getUsername());

                            // Сохранение нового refresh-токена в базе данных
                            refreshToken.setTokenValue(newRefreshTokenValue);
                            User user = userRepository.findByUsername(username);
                            RefreshToken newRefreshToken = new RefreshToken(newRefreshTokenValue, user);
                            refreshTokenService.save(newRefreshToken);
//                            refreshTokenService.deleteByTokenValue(refreshTokenValue);
                            Cookie tokenCookie = new Cookie("jwt", newJwt);
                            tokenCookie.setMaxAge(24 * 60 * 60); // Время жизни токена в секундах (здесь 24 часа)
                            tokenCookie.setPath("/");
                            response.addCookie(tokenCookie);

                            // Установка нового refresh-токена в куку
                            Cookie refreshTokenCookie = new Cookie("refreshToken", newRefreshTokenValue);
                            refreshTokenCookie.setMaxAge(24 * 60 * 60); // Время жизни токена в секундах (здесь 24 часа)
                            refreshTokenCookie.setPath("/");
                            response.addCookie(refreshTokenCookie);

                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        } else {
                            Cookie cookie = new Cookie("jwt", null);
                            cookie.setMaxAge(0);
                            cookie.setPath("/");
                            response.addCookie(cookie);
                            Cookie refreshTokenCookie = new Cookie("refreshToken", null);
                            refreshTokenCookie.setMaxAge(0);
                            refreshTokenCookie.setPath("/");
                            response.addCookie(refreshTokenCookie);

                            SecurityContextHolder.clearContext();
                        }
                    } else {
                        Cookie cookie = new Cookie("jwt", null);
                        cookie.setMaxAge(0);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
                        refreshTokenCookie.setMaxAge(0);
                        refreshTokenCookie.setPath("/");
                        response.addCookie(refreshTokenCookie);
                        SecurityContextHolder.clearContext(); //
                    }
                }
            }
//            if (jwt == null) {
//                SecurityContextHolder.clearContext();
//            }
//            if (!jwtUtils.isValidToken(jwt)) {
//                SecurityContextHolder.clearContext();
//            }

            System.err.println(e);

        }
        filterChain.doFilter(request, response);
    }

    private String parseJwtFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
