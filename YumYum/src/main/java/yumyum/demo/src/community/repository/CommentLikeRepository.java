package yumyum.demo.src.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yumyum.demo.config.Status;
import yumyum.demo.src.community.entity.CommentEntity;
import yumyum.demo.src.community.entity.CommentLikeEntity;
import yumyum.demo.src.user.entity.UserEntity;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLikeEntity, Long> {
    Long countCommentLikeEntityByUserAndComment(UserEntity userEntity, CommentEntity commentEntity);
    Optional<CommentLikeEntity> findCommentLikeEntityByUserAndComment(UserEntity userEntity, CommentEntity commentEntity);
    boolean existsByCommentAndUserAndStatus(CommentEntity commentEntity, UserEntity userEntity, Status status);

}
