package com.chosun.demoversion.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KeywordRequest {
    private String targetUrl;
    private String keyword;
    private boolean isNotifyEnabled; // 알림 켜기/끄기
    private String notifyChannel; // 알림 채널
}
