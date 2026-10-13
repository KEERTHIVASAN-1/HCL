package com.hclhostel.config;

import com.hclhostel.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/admin/login", "/warden/login", "/student/login",
                        "/perform_login", "/webjars/**", "/css/**", "/js/**", "/error").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/warden/**").hasRole("WARDEN")
                .requestMatchers("/student/**").hasRole("STUDENT")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/perform_login")
                .successHandler(roleSuccessHandler())
                .failureHandler(roleFailureHandler())
                .permitAll()
            )
            .userDetailsService(userDetailsService)
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/access-denied"))
            .logout(logout -> logout.logoutSuccessUrl("/login?logout"));
        return http.build();
    }

    private AuthenticationSuccessHandler roleSuccessHandler() {
        return (request, response, authentication) -> {
            String role = authentication.getAuthorities().stream()
                    .filter(a -> a.getAuthority().startsWith("ROLE_"))
                    .map(a -> a.getAuthority().substring(5))
                    .findFirst()
                    .orElse("");
            String dashboardPath = switch (role) {
                case "ADMIN" -> "/admin/dashboard";
                case "WARDEN" -> "/warden/dashboard";
                case "STUDENT" -> "/student/dashboard";
                default -> null;
            };
            if (dashboardPath == null) {
                request.getSession().invalidate();
                response.sendRedirect(request.getContextPath() + "/login?error=unsupported");
                return;
            }
            response.sendRedirect(request.getContextPath() + dashboardPath);
        };
    }

    private AuthenticationFailureHandler roleFailureHandler() {
        return (request, response, exception) -> {
            String reason = (exception instanceof DisabledException) ? "inactive" : "bad";
            response.sendRedirect(request.getContextPath() + "/login?error=" + reason);
        };
    }
}
