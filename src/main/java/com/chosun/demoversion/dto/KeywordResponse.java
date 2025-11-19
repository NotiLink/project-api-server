package com.chosun.demoversion.dto;

import com.chosun.demoversion.domain.Keyword;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KeywordResponse {
    private Long id;
    private String targetUrl;
    private String keyword;
    @JsonProperty("isNotifyEnabled")
    private Boolean isNotifyEnabled;
    private String notifyChannel;

    public KeywordResponse(Keyword keyword) {
        this.id = keyword.getId();
        this.targetUrl = keyword.getTargetUrl();
        this.keyword = keyword.getKeyword();
        this.isNotifyEnabled = keyword.isNotifyEnabled();
        this.notifyChannel = keyword.getNotifyChannel();
    }

    public static KeywordResponse from(Keyword keyword) {
        return new KeywordResponse(keyword);
    }
}
