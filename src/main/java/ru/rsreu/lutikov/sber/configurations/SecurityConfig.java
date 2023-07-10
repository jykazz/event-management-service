/**
 * Configuration class for Spring Security.
 *
 * <p>
 * This class provides security configuration options for Spring Security, including URL access rules, password encoding,
 * and user details service configuration.
 * </p>
 *
 * <p>
 * The class extends the {@link org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter}
 * class to customize the Spring Security configuration.
 * </p>
 *
 * <p>
 * The following features are provided by this class:
 * <ul>
 *     <li>URL access rules based on user roles</li>
 *     <li>Password encoding for user authentication</li>
 *     <li>User details service configuration</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>Author:</strong> Vadim
 * <br>
 * <strong>Email:</strong> blinvadik@mail.ru
 * </p>
 *
 * @see org.springframework.context.annotation.Configuration
 * @see org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
 * @see org.springframework.beans.factory.annotation.Autowired
 * @see javax.sql.DataSource
 * @see org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
 * @see org.springframework.security.config.annotation.web.builders.HttpSecurity
 * @see org.springframework.security.crypto.password.PasswordEncoder
 * @see org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
 * @see org.springframework.security.core.userdetails.User
 * @see org.springframework.security.core.userdetails.UserDetails
 * @see org.springframework.security.core.userdetails.UserDetailsService
 * @see org.springframework.security.provisioning.InMemoryUserDetailsManager
 * @see ru.rsreu.lutikov.sber.domain.Role
 * @see ru.rsreu.lutikov.sber.services.UserDetailsServiceImpl
 * @since 2023
 */


package ru.rsreu.lutikov.sber.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.rsreu.lutikov.sber.domain.Role;
import ru.rsreu.lutikov.sber.services.UserDetailsServiceImpl;

import javax.sql.DataSource;

/**
 * Конфигурация безопасности Spring Security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    /**
     * Configures URL access rules and their permissions.
     *
     * @param http the HttpSecurity object for configuring access
     * @throws Exception if an error occurs during configuration
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/events").permitAll()
                .antMatchers("/registration").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/style.css").permitAll()
                .antMatchers("/favicon.ico").permitAll()
//                .antMatchers("/admin/**").hasRole(Role.ADMIN.name())
                .antMatchers("/organizer/**").hasRole(Role.ORGANIZER.name())
                .antMatchers("/tickets").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                .antMatchers("/events/new").hasAnyRole(Role.ADMIN.name())
//                .antMatchers("/users").hasAnyRole(Role.ADMIN.name())
                .antMatchers("/users/**").hasAnyRole(Role.ADMIN.name())
                .antMatchers("/reviews").hasAnyRole(Role.USER.name(), Role.ADMIN.name(), Role.ORGANIZER.name())
                .antMatchers("/user/**").hasAnyRole(Role.USER.name(), Role.ADMIN.name(), Role.ORGANIZER.name())
                .anyRequest()
                        .authenticated()
                    .and()
                        .formLogin().loginPage("/login").permitAll()
                    .and()
                        .logout().logoutUrl("/logout").permitAll();
    }

    /**
     * Bean for creating a PasswordEncoder for password encryption.
     *
     * @return a PasswordEncoder object for password encryption
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean for creating a UserDetailsService for working with user data.
     *
     * @return a UserDetailsService object for working with user data
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    /**
     * Configures the AuthenticationManagerBuilder for user authentication.
     *
     * @param auth the AuthenticationManagerBuilder object for configuring authentication
     * @throws Exception if an error occurs during configuration
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

}
