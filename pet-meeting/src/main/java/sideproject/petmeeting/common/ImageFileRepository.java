package sideproject.petmeeting.common;

import org.springframework.data.jpa.repository.JpaRepository;
import sideproject.petmeeting.post.domain.Post;

public interface ImageFileRepository extends JpaRepository<Post, Long> {
}
