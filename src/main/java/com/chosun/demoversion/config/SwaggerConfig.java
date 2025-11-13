package com.chosun.demoversion.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
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
