package ru.rsreu.lutikov.sber.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
     * Конфигурация доступа к различным URL-адресам и их разрешениям.
     *
     * @param http HttpSecurity объект для настройки доступа
     * @throws Exception Исключение, если произошла ошибка при настройке
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/")
                .permitAll()
                .antMatchers("/events")
                .permitAll().antMatchers("/registration")
                .permitAll().antMatchers("/login")
                .permitAll().antMatchers("/style.css")
                .permitAll().antMatchers("/favicon.ico")
                .permitAll().antMatchers("/admin/**").hasRole(Role.ADMIN.name())
                .antMatchers("/organizer/**").hasRole(Role.ORGANIZER.name())
                .antMatchers("/tickets").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                .antMatchers("/events/new").hasAnyRole(Role.ADMIN.name())
                .antMatchers("/users").hasAnyRole(Role.ADMIN.name())
                .antMatchers("/users/**").hasAnyRole(Role.ADMIN.name())
                .antMatchers("/reviews").hasAnyRole(Role.USER.name(), Role.ADMIN.name(), Role.ORGANIZER.name())
                .antMatchers("/user/**").hasAnyRole(Role.USER.name(), Role.ADMIN.name(), Role.ORGANIZER.name())
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .permitAll();
    }

    /**
     * Bean для создания PasswordEncoder для шифрования паролей.
     *
     * @return PasswordEncoder объект для шифрования паролей
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public UserDetailsService test(PasswordEncoder encoder) {
//        UserDetails admin = User.builder().username("name").password(encoder.encode("pass")).roles("ADMIN").build();
//        return new InMemoryUserDetailsManager(admin);
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .passwordEncoder(new BCryptPasswordEncoder())
//                .usersByUsernameQuery("select username, password, active from users where username = ?")
//                .authoritiesByUsernameQuery("select u.username, ur.roles as role from users u inner join user_role ur on u.id = ur.user_id where u.username=?");
//    }

    /**
     * Bean для создания UserDetailsService для работы с данными пользователей.
     *
     * @return UserDetailsService объект для работы с данными пользователей
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    /**
     * Конфигурация AuthenticationManagerBuilder для аутентификации пользователей.
     *
     * @param auth AuthenticationManagerBuilder объект для настройки аутентификации
     * @throws Exception Исключение, если произошла ошибка при настройке
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

}
