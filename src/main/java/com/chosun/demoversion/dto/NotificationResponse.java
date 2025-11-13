package com.chosun.demoversion.dto;

import com.chosun.demoversion.domain.Notification;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotificationResponse {
    private Long id;
    private String content;
    private String contentUrl;
    private boolean isRead;
    private LocalDateTime createdAt;
    private Long keywordId; // 어떤 키워드로 인한 알림인지 알려줌

    public NotificationResponse(Notification notification) {
        this.id = notification.getId();
        this.content = notification.getContent();
        this.contentUrl = notification.getContentUrl();
        this.isRead = notification.isRead();
        this.createdAt = notification.getCreatedAt();
        this.keywordId = notification.getKeyword().getId();
    }

    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(notification);
    }
}
