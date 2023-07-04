package ru.rsreu.lutikov.sber.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.rsreu.lutikov.sber.domain.Event;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAll();

    @Query("SELECT e FROM Event e WHERE LOWER(e.eventName) LIKE %:search%")
    Page<Event> searchEventsByName(@Param("search") String search, Pageable pageable);

    Page<Event> findByEventNameContainingIgnoreCase(String search, Pageable pageable);

    // Дополнительные методы
}
