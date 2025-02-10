package by.grodmir.online_forum.repositories;

import by.grodmir.online_forum.entities.Notification;
import by.grodmir.online_forum.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    public List<Notification> findByReceiverUsernameOrderByCreatedAtDesc(String username);
}
