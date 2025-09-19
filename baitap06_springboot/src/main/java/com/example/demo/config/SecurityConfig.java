package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Tắt CSRF để form login đơn giản
            .csrf(csrf -> csrf.disable())

            // Cấu hình quyền truy cập
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/WEB-INF/**", "/resources/**", "/uploads/**").permitAll()
                .anyRequest().authenticated()
            )

            // Cấu hình form login
            .formLogin(form -> form
                .loginPage("/login")           // Trang login custom
                .loginProcessingUrl("/login")  // URL submit form login
                .defaultSuccessUrl("/home", true)
                .permitAll()
            )

            // Cấu hình logout
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}
