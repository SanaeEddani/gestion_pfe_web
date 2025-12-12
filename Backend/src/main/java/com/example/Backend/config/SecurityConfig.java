package com.example.Backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // 🔓 Ces deux routes n'ont pas besoin d'être authentifiées
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/auth/password/forgot").permitAll()
                        .requestMatchers("/auth/password/send-otp").permitAll()
                        .requestMatchers("auth/password/verify-otp").permitAll()
                        .requestMatchers("/auth/password/reset").permitAll()

                        // 🔒 Tout le reste nécessite un token (auth obligatoire)
                        .anyRequest().authenticated()
                );

        return http.build();
    }

}
