package by.grodmir.online_forum.repository;

import by.grodmir.online_forum.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByTopicId(Integer topicId);
}
