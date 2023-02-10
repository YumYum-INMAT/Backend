package yumyum.demo.src.user.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yumyum.demo.config.LogInType;
import yumyum.demo.config.Status;
import yumyum.demo.src.user.entity.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @EntityGraph(attributePaths = "authorities")
    Optional<UserEntity> findOneWithAuthoritiesByUsername(String username);

    Optional<UserEntity> findUserEntityByUsername(String username);

    Optional<UserEntity> findUserEntityByUsernameAndStatus(String username, Status status);

    Optional<UserEntity> findUserEntityByEmail(String email);

    Optional<UserEntity> findUserEntityByNickNameAndStatus(String nickName, Status status);

    List<UserEntity> findAllByEmail(String email);

    Optional<UserEntity> findUserEntityByLogInTypeAndSnsId(LogInType logInType, String snsId);
}
