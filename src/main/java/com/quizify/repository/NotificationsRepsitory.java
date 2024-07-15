package com.quizify.repository;

import com.quizify.model.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationsRepsitory extends JpaRepository<Notifications, Long> {

}
