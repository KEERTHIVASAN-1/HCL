package com.hclhostel.config;

import com.hclhostel.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
                .requestMatchers("/admin/login", "/warden/login", "/student/login",
                        "/perform_login", "/webjars/**", "/css/**", "/js/**", "/error").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/warden/**").hasRole("WARDEN")
                .requestMatchers("/student/**").hasRole("STUDENT")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginProcessingUrl("/perform_login")
                .successHandler(roleSuccessHandler())
                .failureHandler(roleFailureHandler())
                .permitAll()
            )
            .userDetailsService(userDetailsService)
            .exceptionHandling(ex -> ex
                .defaultAuthenticationEntryPointFor(
                    new LoginUrlAuthenticationEntryPoint("/admin/login"),
                    new AntPathRequestMatcher("/admin/**"))
                .defaultAuthenticationEntryPointFor(
                    new LoginUrlAuthenticationEntryPoint("/warden/login"),
                    new AntPathRequestMatcher("/warden/**"))
                .defaultAuthenticationEntryPointFor(
                    new LoginUrlAuthenticationEntryPoint("/student/login"),
                    new AntPathRequestMatcher("/student/**"))
                .accessDeniedPage("/access-denied"))
            .logout(logout -> logout.logoutSuccessUrl("/student/login?logout"));
        return http.build();
    }

    private AuthenticationSuccessHandler roleSuccessHandler() {
        return (request, response, authentication) -> {
            String intended = request.getParameter("intendedRole");
            String actual = authentication.getAuthorities().stream()
                    .filter(a -> a.getAuthority().startsWith("ROLE_"))
                    .map(a -> a.getAuthority().substring(5)).findFirst().orElse("");
            String loginPath = loginPath(intended);
            if (loginPath == null || !intended.equals(actual)) {
                request.getSession().invalidate();
                response.sendRedirect((loginPath == null ? "/student/login" : loginPath) + "?error=role");
                return;
            }
            response.sendRedirect("/" + intended.toLowerCase() + "/dashboard");
        };
    }

    private AuthenticationFailureHandler roleFailureHandler() {
        return (request, response, exception) -> {
            String loginPath = loginPath(request.getParameter("intendedRole"));
            String reason = (exception instanceof DisabledException) ? "inactive" : "bad";
            response.sendRedirect((loginPath == null ? "/student/login" : loginPath) + "?error=" + reason);
        };
    }

    private String loginPath(String role) {
        if (role == null) return null;
        return switch (role) {
            case "ADMIN" -> "/admin/login";
            case "WARDEN" -> "/warden/login";
            case "STUDENT" -> "/student/login";
            default -> null;
        };
    }
}
