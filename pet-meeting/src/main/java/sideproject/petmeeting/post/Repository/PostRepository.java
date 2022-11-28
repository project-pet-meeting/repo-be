package sideproject.petmeeting.post.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sideproject.petmeeting.post.domain.Post;
import sideproject.petmeeting.post.dto.PostResponseDto;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<PostResponseDto> findAllByOrderByModifiedAtDesc(Pageable pageable);

}
