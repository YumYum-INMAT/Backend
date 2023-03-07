package yumyum.demo.src.restaurant.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import yumyum.demo.config.BaseEntity;
import yumyum.demo.src.community.entity.PostEntity;
import yumyum.demo.src.user.entity.UserEntity;

@Entity
@Getter
@Setter
@Table(name = "reviewReport")
@NoArgsConstructor
@DynamicInsert
public class ReviewReportEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporting_user_id", referencedColumnName = "user_id")
    private UserEntity reportingUserEntity; // 신고하는 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private ReviewEntity reviewEntity; // 신고 당한 리뷰

    @Column(nullable = false)
    private String contents; // 신고 내용

    public ReviewReportEntity(UserEntity reportingUserEntity, ReviewEntity reviewEntity, String contents) {
        this.reportingUserEntity = reportingUserEntity;
        this.reviewEntity = reviewEntity;
        this.contents = contents;
    }
}
