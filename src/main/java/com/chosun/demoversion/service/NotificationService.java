package com.chosun.demoversion.service;

import com.chosun.demoversion.domain.Notification;
import com.chosun.demoversion.domain.User;
import com.chosun.demoversion.dto.NotificationResponse;
import com.chosun.demoversion.dto.UnreadCountResponse;
import com.chosun.demoversion.repository.NotificationRepository;
import com.chosun.demoversion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<NotificationResponse> getMyNotifications(String email) {
        User user = findUserByEmail(email);

        return notificationRepository.findAllByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UnreadCountResponse getUnreadCount(String email) {
        User user = findUserByEmail(email);
        long count = notificationRepository.countByUserAndIsReadFalse(user);
        return new UnreadCountResponse(count);
    }

    @Transactional
    public void markAsRead(String email, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 알림입니다."));

        // 알림의 주인과 로그인한 사용자가 일치하는지 확인
        if (!notification.getUser().getEmail().equals(email)) {
            throw new SecurityException("알림을 읽을 권한이 없습니다.");
        }

        // 엔티티의 읽음 처리 메서드 호출
        notification.markAsRead();
        // @Transactional에 의해 메서드 종료 시 자동으로 DB에 update 됨
    }

    // 공통 로직
    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }
}
