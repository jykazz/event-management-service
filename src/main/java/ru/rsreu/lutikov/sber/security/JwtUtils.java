package ru.rsreu.lutikov.sber.security;

import java.util.Date;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import ru.rsreu.lutikov.sber.repositories.UserRepository;
import org.springframework.security.core.userdetails.User;

@Component
public class JwtUtils {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Autowired
    UserRepository userRepository;

    public String generateJwtToken(Authentication authentication) {

        User userPrincipal = (User) authentication.getPrincipal();

        return Jwts.builder().setSubject((userPrincipal.getUsername())).claim("role", userRepository.findByUsername(userPrincipal.getUsername()).getRoles()
                ).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public boolean validateJwtToken(String jwt) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
            return true;
        } catch (MalformedJwtException e) {
            System.err.println(e.getMessage());
            SecurityContextHolder.clearContext();
            return false;
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            SecurityContextHolder.clearContext();
            return false;
        }

    }

    public String getUserNameFromJwtToken(String jwt) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody().getSubject();
    }

    public String getUserRoleFromJwtToken(String jwt) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody();
        return (String) claims.get("role");
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date expirationDate = claims.getExpiration();
            return expirationDate.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token);

            // Проверка подписи токена
            if (!Jwts.parserBuilder().setSigningKey(jwtSecret).build().isSigned(token)) {
                return false;
            }

            return true;
        } catch (JwtException e) {
            // Обработка исключений, если произошла ошибка валидации токена
            System.err.println("Invalid token: " + e.getMessage());
            return false;
        }
    }


}
