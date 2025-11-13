package com.chosun.demoversion.controller;

import com.chosun.demoversion.dto.KeywordRequest;
import com.chosun.demoversion.dto.KeywordResponse;
import com.chosun.demoversion.service.KeywordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "2. 알림 키워드 (Keyword)")
@SecurityRequirement(name = "Bearer Authentication") // Swagger에서 인증이 필요함을 명시
@RestController
@RequestMapping("/api/keywords")
@RequiredArgsConstructor
public class KeywordController {

    private final KeywordService keywordService;

    @Operation(summary = "키워드 등록", description = "새로운 알림 키워드를 등록합니다. (인증 필요)")
    @PostMapping
    public ResponseEntity<KeywordResponse> createKeyword(@AuthenticationPrincipal String email, // JwtAuthenticationFilter에서 저장할 이메일을 가져옴
                                                         @RequestBody KeywordRequest request) {
        KeywordResponse response = keywordService.createKeyword(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "내 키워드 목록 조회", description = "내가 등록한 모든 키워드를 조회합니다. (인증 필요)")
    @GetMapping
    public ResponseEntity<List<KeywordResponse>> getMyKeywords(@AuthenticationPrincipal String email) {
        List<KeywordResponse> response = keywordService.getMyKeywords(email);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "키워드 삭제", description = "내가 등록한 키워드를 삭제합니다. (인증 필요)")
    @DeleteMapping("/{keywordId}")
    public ResponseEntity<Void> deleteKeyword(@AuthenticationPrincipal String email, @PathVariable Long keywordId) {
        keywordService.deleteKeyword(email, keywordId);
        return ResponseEntity.noContent().build();
    }
}
