package ru.rsreu.lutikov.sber.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rsreu.lutikov.sber.domain.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAll();

    List<Review> findByEventId(Long eventId);

    List<Review> findByUserId(Long userId);

    // Дополнительные методы
}
