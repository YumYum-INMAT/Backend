package yumyum.demo.src.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yumyum.demo.src.restaurant.entity.ReviewReportEntity;

@Repository
public interface ReviewReportRepository extends JpaRepository<ReviewReportEntity, Long> {
}
