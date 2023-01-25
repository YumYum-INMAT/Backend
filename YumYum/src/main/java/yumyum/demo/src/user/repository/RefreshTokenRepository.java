package yumyum.demo.src.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import yumyum.demo.config.Status;
import yumyum.demo.src.user.entity.RefreshTokenEntity;
import yumyum.demo.src.user.entity.UserEntity;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findRefreshTokenEntityByUserAndStatus(UserEntity user, Status status);

    Optional<RefreshTokenEntity> findRefreshTokenEntityByUserAgentAndStatus(String userAgent, Status status);
}
