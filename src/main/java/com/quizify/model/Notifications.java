package com.quizify.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
@Entity
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")

    private long notificationId;

    private String title;

    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "received_by")
    private long receivedBy;

    private String link;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        modifiedAt = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User user;

}
