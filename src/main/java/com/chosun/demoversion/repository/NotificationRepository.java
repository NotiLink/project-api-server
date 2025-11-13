package com.chosun.demoversion.repository;

import com.chosun.demoversion.domain.Notification;
import com.chosun.demoversion.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 특정 사용자의 모든 알림을 최신순으로 조회
    List<Notification> findAllByUserOrderByCreatedAtDesc(User user);

    // 특정 사용자의 읽지 않은 알림 개수 조회
    long countByUserAndIsReadFalse(User user);
}
