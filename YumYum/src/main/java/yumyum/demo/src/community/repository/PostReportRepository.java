package yumyum.demo.src.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yumyum.demo.src.community.entity.PostEntity;
import yumyum.demo.src.community.entity.PostReportEntity;

@Repository
public interface PostReportRepository extends JpaRepository<PostReportEntity, Long> {

}
