package com.quizify.service;

import com.quizify.model.*;
import com.quizify.repository.NotificationsRepsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationsService {
    @Autowired
    private NotificationsRepsitory notificationsRepsitory;

    public List<Notifications> getAllNotifications(){
        return notificationsRepsitory.findAll();
    }

    public List<Notifications> getNotificationsByReceiver(User user) {
        return notificationsRepsitory.getNotificationsByReceivedBy(user);
    }

    public void saveNotification(String title,String content,User createdBy,long receivedBy,String link,boolean read){
        Notifications notifi = new Notifications();
        notifi.setTitle(title);
        notifi.setContent(content);
        notifi.setUser(createdBy);
        notifi.setReceivedBy(receivedBy);
        notifi.setLink(link);
        notifi.setRead(read);
        notificationsRepsitory.save(notifi);
    }

    public void deleteNotifi(long id){
        Notifications noti = notificationsRepsitory.findById(id).orElse(null);
        if (noti != null){
            notificationsRepsitory.delete(noti);
        }
    }
}
