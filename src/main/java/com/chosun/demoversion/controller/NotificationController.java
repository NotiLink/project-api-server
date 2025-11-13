package com.chosun.demoversion.controller;

import com.chosun.demoversion.dto.NotificationResponse;
import com.chosun.demoversion.dto.UnreadCountResponse;
import com.chosun.demoversion.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "3. 알림 내역 (Notification)")
@SecurityRequirement(name = "Bearer Authentication") // 인증 필요
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "내 알림 목록 조회", description = "내가 받은 모든 알림을 최신순으로 조회합니다. (인증 필요)")
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(
            @AuthenticationPrincipal String email // 인증된 사용자 이메일
    ) {
        List<NotificationResponse> response = notificationService.getMyNotifications(email);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "읽지 않은 알림 개수 조회", description = "읽지 않은 알림의 개수를 조회 (인증 필요)")
    @GetMapping("/unread-count")
    public ResponseEntity<UnreadCountResponse> getUnreadCount(
            @AuthenticationPrincipal String email
    ) {
        UnreadCountResponse response = notificationService.getUnreadCount(email);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "알림 읽음 처리", description = "특정 알림을 '읽음' 상태로 변경합니다. (인증 필요)")
    @PatchMapping("/{notificationId}/read") // PATCH 메서드 사용
    public ResponseEntity<Void> markAsRead(
            @AuthenticationPrincipal String email,
            @PathVariable Long notificationId
    ) {
        notificationService.markAsRead(email, notificationId);
        return ResponseEntity.noContent().build(); // 성공 시 204 No Content
    }
}


