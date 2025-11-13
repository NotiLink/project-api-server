package com.chosun.demoversion.repository;

import com.chosun.demoversion.domain.Keyword;
import com.chosun.demoversion.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    // 특정 유저가 등록한 모든 키워드를 조회
    List<Keyword> findAllByUser(User user);
}
