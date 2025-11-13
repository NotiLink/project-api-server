package com.chosun.demoversion.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // csrf 비활성화 (api 서버이므로)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안함 (jwt 사용)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**").permitAll() // 1. /api/auth/** 경로는 모두 허용 (회원가입, 로그인)
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll() // 2. Swagger 경로는 모두 허용
                        .requestMatchers(PathRequest.toH2Console()).permitAll() // H2 콘솔 접근 허용
                        .anyRequest().authenticated() // 3. 그 외 모든 요청은 인증(로그인) 필요
                )
                // 나중에 여기에 JwtAuthenticationFilter 추가 해야 함
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())
                );
        return http.build();
    }
}
