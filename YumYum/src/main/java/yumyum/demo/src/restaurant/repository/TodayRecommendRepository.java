package yumyum.demo.src.restaurant.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import yumyum.demo.config.Status;
import yumyum.demo.src.restaurant.dto.TodayRecommendDto;
import yumyum.demo.src.restaurant.entity.TodayRecommendEntity;

@Repository
public interface TodayRecommendRepository extends JpaRepository<TodayRecommendEntity, Long> {
    @Query(value = "SELECT * FROM today_recommend as tr  where status = 'ACTIVE' order by RAND() limit 3",nativeQuery = true)
    List<TodayRecommendEntity> findTop3ByStatus();
}
