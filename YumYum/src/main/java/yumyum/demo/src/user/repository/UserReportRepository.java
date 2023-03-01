package yumyum.demo.src.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yumyum.demo.src.user.entity.UserEntity;
import yumyum.demo.src.user.entity.UserReportEntity;

@Repository
public interface UserReportRepository extends JpaRepository<UserReportEntity, Long> {
}
