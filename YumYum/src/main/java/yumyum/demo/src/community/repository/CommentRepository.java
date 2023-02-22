package yumyum.demo.src.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yumyum.demo.config.Status;
import yumyum.demo.src.community.entity.CommentEntity;
import yumyum.demo.src.community.entity.PostEntity;
import yumyum.demo.src.user.entity.UserEntity;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    CommentEntity findCommentEntityByUserAndPost(UserEntity userEntity, PostEntity postEntity);

    Optional<CommentEntity> findCommentEntityByIdAndStatus(Long commentId, Status status);
}
