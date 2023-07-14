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
            } else if (jwtUtils.isTokenExpired(jwt)) {
                SecurityContextHolder.clearContext();
            } else if (jwt == null) {
                SecurityContextHolder.clearContext();
            } else if (!jwtUtils.isValidToken(jwt)) {
                SecurityContextHolder.clearContext();
            }

        } catch (Exception e) {
            String jwt = parseJwtFromCookies(request);
            if (jwtUtils.isTokenExpired(jwt)) {
                SecurityContextHolder.clearContext();
            }
            if (jwt == null) {
                SecurityContextHolder.clearContext();
            }
            if (!jwtUtils.isValidToken(jwt)) {
                SecurityContextHolder.clearContext();
            }

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
}
