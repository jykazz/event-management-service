/**
 * Configuration class for Spring MVC.
 *
 * <p>
 * This class provides configuration options for Spring MVC, such as CORS mappings and view controllers.
 * </p>
 *
 * <p>
 * The class implements the {@link org.springframework.web.servlet.config.annotation.WebMvcConfigurer} interface
 * to customize the Spring MVC configuration.
 * </p>
 *
 * <p>
 * The following features are provided by this class:
 * <ul>
 *     <li>CORS configuration to allow cross-origin requests for all endpoints</li>
 *     <li>View controller mappings for common paths</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Author:</strong> Vadim
 * <br>
 * <strong>Email:</strong> blinvadik@mail.ru
 * </p>
 *
 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurer
 * @see org.springframework.web.servlet.config.annotation.CorsRegistry
 * @see org.springframework.web.servlet.config.annotation.ViewControllerRegistry
 * @since 2023
 */
package ru.rsreu.lutikov.sber.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * Configures the CORS (Cross-Origin Resource Sharing) mappings.
     *
     * <p>
     * This method allows all origins and specifies the allowed HTTP methods as GET, POST, PUT, and DELETE.
     * </p>
     *
     * @param registry the {@link org.springframework.web.servlet.config.annotation.CorsRegistry} instance
     *                 used to register the CORS mappings
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "PUT", "DELETE");
    }

    /**
     * Configures the view controllers for common paths.
     *
     * <p>
     * This method adds view controllers for the following paths:
     * <ul>
     *     <li>{@code /home} - maps to the "home" view</li>
     *     <li>{@code /} - maps to the "home" view</li>
     *     <li>{@code /hello} - maps to the "hello" view</li>
     *     <li>{@code /login} - maps to the "login" view</li>
     * </ul>
     * </p>
     *
     * @param registry the {@link org.springframework.web.servlet.config.annotation.ViewControllerRegistry} instance
     *                 used to register the view controllers
     */
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }
}