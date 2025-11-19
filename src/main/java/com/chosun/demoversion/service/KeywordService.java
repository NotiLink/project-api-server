package com.chosun.demoversion.service;

import com.chosun.demoversion.domain.Keyword;
import com.chosun.demoversion.domain.User;
import com.chosun.demoversion.dto.KeywordRequest;
import com.chosun.demoversion.dto.KeywordResponse;
import com.chosun.demoversion.repository.KeywordRepository;
import com.chosun.demoversion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;
    private final UserRepository userRepository;

    @Transactional
    public KeywordResponse createKeyword(String email, KeywordRequest request) {
        // 1. email을 기준으로 User 엔티티를 찾음
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        String keywordVal = request.getKeyword();
        if(keywordVal != null && keywordVal.trim().isEmpty()) {
            keywordVal = null;
        }

        // 2. Keyword 엔티티를 생성
        Keyword keyword = Keyword.builder()
                .user(user)
                .targetUrl(request.getTargetUrl())
                .keyword(keywordVal)
                .isNotifyEnabled(request.getNotifyEnabled())
                .notifyChannel(request.getNotifyChannel())
                .build();

        // 3. 저장
        keywordRepository.save(keyword);

        return KeywordResponse.from(keyword);
    }

    @Transactional
    public List<KeywordResponse> getMyKeywords(String email) {
        // 1. email을 기준으로 User 엔티티를 찾음
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 2. 해당 User가 등록한 모든 키워드를 찾음
        List<Keyword> keywords = keywordRepository.findAllByUser(user);

        // 3. DTO로 변환하여 반환함
        return keywords.stream()
                .map(KeywordResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteKeyword(String email, Long keywordId) {
        // 1. 키워드를 찾음
        Keyword keyword = keywordRepository.findById(keywordId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 키워드입니다."));

        // 2. 키워드를 등록한 사용자와 현재 로그인한 사용자가 일치하는지 확인
        if (!keyword.getUser().getEmail().equals(email)) {
            throw new SecurityException("키워드를 삭제할 권한이 없습니다.");
        }

        // 3. 삭제
        keywordRepository.delete(keyword);
    }
}
