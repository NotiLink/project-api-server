package com.chosun.demoversion.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Request Header에서 토큰 추출
        String token = resolveToken(request);

        // [디버깅 로그]
        log.info("[Filter] 요청 URL: {}", request.getRequestURI());
        log.info("[Filter] 추출된 토큰: {}", (token != null ? token : "NULL (헤더 없음/형식 불일치)"));

        // 2. validateToken으로 토큰 유효성 검사
        if (token != null && jwtTokenProvider.validateToken(token)) {

            // [디버깅 로그]
            log.info("[Filter] 토큰 유효성 검사 통과!");

            String email = jwtTokenProvider.getEmail(token);
            log.info("[Filter] 토큰 주인(Email): {}", email);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    email, null, Collections.emptyList()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } else if (token != null) {
            // [디버깅 로그 - 에러 레벨]
            log.error("[Filter] 토큰이 유효하지 않습니다! (만료/서명 불일치)");
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}