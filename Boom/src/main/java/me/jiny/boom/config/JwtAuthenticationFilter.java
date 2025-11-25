package me.jiny.boom.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jiny.boom.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String bearerToken = request.getHeader("Authorization");

            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                String token = bearerToken.substring(7);

                if (jwtUtil.validateToken(token)) {
                    Long userId = jwtUtil.getUserIdFromToken(token);

                    UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(
                                    userId,
                                    null,
                                    new ArrayList<>()
                            );

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.debug("JWT 인증 성공: 사용자 ID {}", userId);
                }
            }
        } catch (Exception e) {
            log.error("JWT 인증 처리 중 오류 발생", e);
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // Swagger 관련 경로 - 가장 먼저 체크
        if (path.startsWith("/swagger-ui/") ||
            path.equals("/swagger-ui.html") ||
            path.startsWith("/v3/api-docs/") ||
            path.startsWith("/swagger-resources/") ||
            path.startsWith("/webjars/")) {
            return true;
        }
        // API 인증 불필요 경로
        if (path.startsWith("/api/auth/") ||
            path.startsWith("/api/categories/") ||
            path.startsWith("/api/keywords/") ||
            path.startsWith("/api/cards/explore/recent") ||
            path.startsWith("/api/cards/explore/ranking") ||
            path.startsWith("/api/cards/users/") ||
            (path.startsWith("/api/users/") && path.endsWith("/statistics"))) {
            return true;
        }
        // 카드 상세 조회: GET /api/cards/{cardId}
        if (path.matches("/api/cards/\\d+") && "GET".equals(request.getMethod())) {
            return true;
        }
        return false;
    }
}

