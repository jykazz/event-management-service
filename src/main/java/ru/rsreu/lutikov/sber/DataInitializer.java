//package ru.rsreu.lutikov.sber;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//import ru.rsreu.lutikov.sber.domain.Event;
//import ru.rsreu.lutikov.sber.domain.Review;
//import ru.rsreu.lutikov.sber.domain.Ticket;
//import ru.rsreu.lutikov.sber.domain.User;
//import ru.rsreu.lutikov.sber.repositories.EventRepository;
//import ru.rsreu.lutikov.sber.repositories.ReviewRepository;
//import ru.rsreu.lutikov.sber.repositories.TicketRepository;
//import ru.rsreu.lutikov.sber.repositories.UserRepository;
//
//import java.time.LocalDateTime;
//
//@Component
//public class DataInitializer implements ApplicationRunner {
//    private final EventRepository eventRepository;
//    private final UserRepository userRepository;
//    private final TicketRepository ticketRepository;
//    private final ReviewRepository reviewRepository;
//
//
//    @Autowired
//    public DataInitializer(UserRepository userRepository, EventRepository eventRepository, TicketRepository ticketRepository, ReviewRepository reviewRepository) {
//        this.userRepository = userRepository;
//        this.eventRepository = eventRepository;
//        this.ticketRepository = ticketRepository;
//        this.reviewRepository = reviewRepository;
//    }
//
////    @Autowired
////    public DataInitializer(EventRepository eventRepository) {
////        this.eventRepository = eventRepository;
////    }
//
//    @Override
//    public void run(ApplicationArguments args) {
//        // Инициализация данных
//        for (int i = 0; i < 10; i++) {
//            String username = "test " + Integer.toString(i);
//            String password = "test";
//            String email = "test-" + Integer.toString(i) + "@test.ru";
//            String role = "USER";
//            User user = new User(username, password, email, role);
//            userRepository.save(user);
//
//
//            String name = "test";
//            String description = "test";
//            LocalDateTime dateTime = LocalDateTime.of(2023, 6, 26, 15, 30, 0);
//            Event event = new Event(name, description, dateTime);
//            eventRepository.save(event);
//
//            Ticket ticket = new Ticket(user, event);
//            ticketRepository.save(ticket);
//
//            Review review = new Review(user, event, "test");
//            reviewRepository.save(review);
//        }
//
//
//    }
//}
//
