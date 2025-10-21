package com.example.catering_system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;
    
    @Autowired
    private CustomAuthenticationFailureHandler failureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/images/**",
                                "/css/**",
                                "/js/**",
                                "/event/**",
                                "/kitchen/**",
                                "/login/customer",
                                "/login/customer-admin",
                                "/customer",
                                "/register/customer",
                                "/register",
                                "/book-event",
                                "/menus.html",
                                "/error"
                        ).permitAll()
                        // Allow readonly access to bookings for event page data loads
                        .requestMatchers(HttpMethod.GET, "/api/bookings", "/api/bookings/**").permitAll()
                        // Allow write/update for event module usage (handled by internal UI)
                        .requestMatchers(HttpMethod.POST, "/api/bookings").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/bookings/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/bookings/**").permitAll()
                        .requestMatchers("/dashboard-customer").authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(login -> login
                        .loginPage("/login/customer")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .successHandler(successHandler)
                        .failureHandler(failureHandler)
                        .permitAll()
                )
                .rememberMe(rm -> rm
                        .key("golden-dish-remember-me-key")
                        .rememberMeParameter("remember")
                        .tokenValiditySeconds(60 * 60 * 24 * 14) // 14 days
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login/customer?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
