package com.quizify.repository;

import com.quizify.model.Notifications;
import com.quizify.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationsRepsitory extends JpaRepository<Notifications, Long> {
    List<Notifications> getNotificationsByReceivedBy(User user);
}
