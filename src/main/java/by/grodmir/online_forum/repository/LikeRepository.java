package by.grodmir.online_forum.repository;

import by.grodmir.online_forum.entity.Like;
import by.grodmir.online_forum.entity.EntityType;
import by.grodmir.online_forum.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findByUserAndEntityIdAndEntityType(User user, Integer entityId, EntityType entityType);
    int countByEntityIdAndEntityTypeAndLiked(Integer entityId, EntityType entityType, Boolean isLike);
}
