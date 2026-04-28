package com.prowork.analytics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // REDIRECCIÓN SEGÚN ROL
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                    HttpServletResponse response,
                    Authentication authentication)
                    throws IOException, ServletException {

                boolean isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                boolean isSupervisor = authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_SUPERVISOR"));

                boolean isOperario = authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_OPERARIO"));

                if (isAdmin) {
                    response.sendRedirect("/admin");
                } else if (isSupervisor) {
                    response.sendRedirect("/supervisor");
                } else if (isOperario) {
                    response.sendRedirect("/operario");
                } else {
                    response.sendRedirect("/login?error");
                }
            }
        };
    }
    // FILTRO DE SEGURIDAD
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                // RUTAS PÚBLICAS
                .requestMatchers("/", "/login", "/conocer", "redirect", "/css/**", "/js/**", "/images/**").permitAll()
                // MODULO INDUSTRIAL (ADMIN + SUPERVISOR)
                .requestMatchers("/industrial/**").hasAnyRole("ADMIN", "SUPERVISOR")
                // ADMIN
                .requestMatchers("/admin/**").hasAnyRole("ADMIN", "SUPERVISOR")
                // SUPERVISOR
                .requestMatchers("/supervisor/**").hasAnyRole("ADMIN", "SUPERVISOR")
                // OPERARIO
                .requestMatchers("/operario/**").hasAnyRole("ADMIN", "OPERARIO")
                // CUALQUIER OTRA RUTA REQUIERE LOGIN
                .anyRequest().authenticated()
                )
                // LOGIN PERSONALIZADO
                .formLogin(form -> form
                .loginPage("/login")
                .successHandler(successHandler())
                .permitAll()
                )
                // LOGOUT
                .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
                );

        return http.build();
    }
}
