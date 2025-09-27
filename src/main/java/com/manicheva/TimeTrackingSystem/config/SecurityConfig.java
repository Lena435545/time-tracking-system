package com.manicheva.TimeTrackingSystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                          .requestMatchers("/admin/**").hasRole("ADMIN")
                          .requestMatchers("/auth/login", "/auth/registration", "/error", "/css/**", "/images/**").permitAll()
                          .anyRequest().authenticated()
                  )
                  .formLogin(form -> form
                          .loginPage("/auth/login")
                          .loginProcessingUrl("/process_login")
                          .defaultSuccessUrl("/", true)
                          .failureUrl("/auth/login?error")
                          .permitAll()
                  )
                  .logout(logout -> logout
                          .logoutUrl("/auth/logout")
                          .logoutSuccessUrl("/auth/login")
                          .invalidateHttpSession(true)
                          .deleteCookies("JSESSIONID")
                          .permitAll()
                  )
                  .exceptionHandling(ex -> ex
                          .accessDeniedPage("/auth/access_denied"))
                  .sessionManagement(session -> session
                          .maximumSessions(1)
                          .maxSessionsPreventsLogin(false));

        return http.build();
    }
}
