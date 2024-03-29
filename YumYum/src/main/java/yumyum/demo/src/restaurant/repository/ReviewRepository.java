package yumyum.demo.src.restaurant.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yumyum.demo.config.Status;
import yumyum.demo.src.restaurant.entity.RestaurantEntity;
import yumyum.demo.src.restaurant.entity.ReviewEntity;
import yumyum.demo.src.user.entity.UserEntity;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    Optional<ReviewEntity> findReviewEntityByIdAndStatus(Long reviewId, Status status);

    List<ReviewEntity> findTop3ByStatusOrderByCreatedAtDesc(Status status);

    List<ReviewEntity> findAllByRestaurantAndStatusOrderByCreatedAtDesc(RestaurantEntity restaurant, Status status);

    List<ReviewEntity> findAllByUserAndStatus(UserEntity user, Status status);
}
