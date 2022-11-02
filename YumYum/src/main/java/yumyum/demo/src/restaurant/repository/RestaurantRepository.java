package yumyum.demo.src.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yumyum.demo.src.restaurant.entity.RestaurantEntity;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

}
