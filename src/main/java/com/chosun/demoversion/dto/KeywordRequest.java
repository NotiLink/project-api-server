package com.chosun.demoversion.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KeywordRequest {
    private String targetUrl;
    private String keyword;
}
