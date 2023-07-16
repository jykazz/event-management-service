package ru.rsreu.lutikov.sber.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rsreu.lutikov.sber.domain.RefreshToken;
import ru.rsreu.lutikov.sber.repositories.RefreshTokenRepository;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken findByTokenValue(String tokenValue) {
        return refreshTokenRepository.findByTokenValue(tokenValue);
    }

    @Transactional
    public void deleteByTokenValue(String tokenValue) {
        refreshTokenRepository.deleteByTokenValue(tokenValue);
    }

    public RefreshToken save(RefreshToken refreshToken) {
        String tokenValue = refreshToken.getTokenValue();
        RefreshToken existingToken = refreshTokenRepository.findByTokenValue(tokenValue);
        if (existingToken == null) {
            return refreshTokenRepository.save(refreshToken);
        } else {
            // Логика обработки случая, когда токен уже существует
            // Можно выбросить исключение или выполнить другие действия по вашему усмотрению
            return null;
        }
    }
}