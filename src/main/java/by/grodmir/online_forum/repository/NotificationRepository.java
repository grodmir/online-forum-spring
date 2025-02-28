package by.grodmir.online_forum.repository;

import by.grodmir.online_forum.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByReceiverUsernameOrderByCreatedAtDesc(String username);
}
