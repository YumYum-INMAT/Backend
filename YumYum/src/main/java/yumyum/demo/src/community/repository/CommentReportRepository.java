package yumyum.demo.src.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yumyum.demo.src.community.entity.CommentReportEntity;

@Repository
public interface CommentReportRepository extends JpaRepository<CommentReportEntity, Long> {

}
