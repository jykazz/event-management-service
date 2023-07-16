package ru.rsreu.lutikov.sber.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.rsreu.lutikov.sber.domain.RefreshToken;


@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    RefreshToken findByTokenValue(String tokenValue);

    @Transactional
    void deleteByTokenValue(String tokenValue);

}
