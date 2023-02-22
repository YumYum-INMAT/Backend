package yumyum.demo.src.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yumyum.demo.src.community.entity.PostEntity;
import yumyum.demo.src.community.entity.PostLikeEntity;
import yumyum.demo.src.user.entity.UserEntity;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLikeEntity, Long> {
    Long countPostLikeEntityByUserAndPost(UserEntity userEntity, PostEntity postEntity);
    Optional<PostLikeEntity> findPostLikeEntityByUserAndPost(UserEntity userEntity, PostEntity postEntity);
}
