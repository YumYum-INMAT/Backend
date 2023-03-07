package yumyum.demo.src.restaurant.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import yumyum.demo.config.Status;
import yumyum.demo.src.restaurant.dto.RestaurantDto;
import yumyum.demo.src.restaurant.entity.RestaurantEntity;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    Optional<RestaurantEntity> findRestaurantEntityByIdAndStatus(Long restaurantId, Status status);
    Optional<RestaurantEntity> findAllByRestaurantNameOrRestaurantMenuEntitiesContaining(String query);

}
