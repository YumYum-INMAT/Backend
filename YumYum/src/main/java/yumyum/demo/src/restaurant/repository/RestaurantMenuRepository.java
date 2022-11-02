package yumyum.demo.src.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yumyum.demo.src.restaurant.entity.RestaurantMenuEntity;

@Repository
public interface RestaurantMenuRepository extends JpaRepository<RestaurantMenuEntity, Long> {

}
