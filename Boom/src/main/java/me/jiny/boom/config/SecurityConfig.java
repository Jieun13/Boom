package me.jiny.boom.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Swagger 관련 경로 - 가장 먼저 매칭
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/swagger-ui/index.html").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/swagger-resources/**").permitAll()
                .requestMatchers("/webjars/**").permitAll()
                // 인증 없이 접근 가능한 경로
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/categories/**").permitAll()
                .requestMatchers("/api/keywords/**").permitAll()
                .requestMatchers("/api/cards/{cardId}").permitAll()
                .requestMatchers("/api/cards/explore/recent").permitAll()
                .requestMatchers("/api/cards/explore/ranking").permitAll()
                .requestMatchers("/api/cards/users/**").permitAll()
                .requestMatchers("/api/users/{userId:\\d+}").permitAll()
                .requestMatchers("/api/users/{userId:\\d+}/statistics").permitAll()
                // 나머지 모든 요청은 인증 필요
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"success\":false,\"message\":\"인증이 필요합니다\"}");
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"success\":false,\"message\":\"접근 권한이 없습니다\"}");
                })
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

