package com.chosun.demoversion.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // 1. JWT 인증 설정 정의 ("Bearer 토큰을 쓸 거야")
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        // 2. 정의한 설정을 적용 ("이 API는 인증이 필요해")
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("Bearer Authentication");

        return new OpenAPI()
                // (중요) 컴포넌트에 보안 스킴 등록
                .components(new Components().addSecuritySchemes("Bearer Authentication", securityScheme))
                // (중요) 전역 보안 요구사항 적용
                .security(List.of(securityRequirement))
                .info(new Info()
                        .title("알림 봇 API 명세서")
                        .version("v1.0.0")
                        .description("팀 프로젝트 알림 봇 API 문서입니다."))
                .tags(List.of(
                        new Tag().name("1. 사용자 인증 (Auth)").description("회원가입, 로그인 API"),
                        new Tag().name("2. 알림 키워드 (Keyword)").description("로그인한 사용자의 키워드 관리 API"),
                        new Tag().name("3. 알림 내역 (Notification)").description("알림 내역 조회 및 관리 API")
                ));
    }
}