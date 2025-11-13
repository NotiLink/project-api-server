package com.chosun.demoversion.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String targetUrl; // 크롤링 할 URL

    @Column(nullable = false)
    private String keyword; // 찾을 키워드

    @Builder
    public Keyword(User user, String targetUrl, String keyword) {
        this.user = user;
        this.targetUrl = targetUrl;
        this.keyword = keyword;
    }
}
