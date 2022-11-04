package yumyum.demo.src.restaurant.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yumyum.demo.src.restaurant.entity.HeartEntity;
import yumyum.demo.src.restaurant.entity.RestaurantEntity;
import yumyum.demo.src.user.entity.UserEntity;

@Repository
public interface HeartRepository extends JpaRepository<HeartEntity, Long> {
    Optional<HeartEntity> findHeartEntityByRestaurantAndUser(RestaurantEntity restaurant, UserEntity user);
}
