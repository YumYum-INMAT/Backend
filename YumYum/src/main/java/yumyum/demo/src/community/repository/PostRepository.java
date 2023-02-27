package yumyum.demo.src.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yumyum.demo.src.community.entity.PostEntity;
import yumyum.demo.src.user.entity.UserEntity;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    //List<PostEntity> findAllByContentsAndTopicContainingOrderByCreatedAtDesc(String contents);
    //List<PostEntity> findAllByContentsAndTopicContaining(String contents);
}
