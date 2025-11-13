package com.chosun.demoversion.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) // (CreateDate 자동 생성을 위해 필요)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 이 알림을 받을 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id", nullable = false)
    private Keyword keyword; // 어떤 키워드로 인해 발생했는지

    @Column(nullable = false, length = 1000)
    private String content; // 알림 내용 (예: "새 공지: [제목]")

    @Column(nullable = false)
    private String contentUrl; // 해당 공지/게시글의 원본 URL

    @Column(nullable = false)
    private boolean isRead = false; // 사용자가 읽었는지 여부

    @Column(nullable = false)
    private boolean isSent = false; // 알림 서버가 발송했는지 여부

    @CreatedDate // 엔티티 생성 시 자동으로 시간 저장
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Notification(User user, Keyword keyword, String content, String contentUrl) {
        this.user = user;
        this.keyword = keyword;
        this.content = content;
        this.contentUrl = contentUrl;
        this.isRead = false; // 생성 시 기본값
        this.isSent = false; // 생성 시 기본값
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
