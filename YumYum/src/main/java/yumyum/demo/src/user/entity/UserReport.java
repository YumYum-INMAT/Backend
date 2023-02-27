package yumyum.demo.src.user.entity;

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
import org.hibernate.annotations.DynamicInsert;
import yumyum.demo.config.BaseEntity;
import yumyum.demo.src.restaurant.entity.RestaurantEntity;

@Entity
@Getter
@Table(name = "userReport")
@NoArgsConstructor
@DynamicInsert
public class UserReport extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporting_user_id", referencedColumnName = "user_id")
    private UserEntity reportingUserEntity; // 신고하는 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id", referencedColumnName = "user_id")
    private UserEntity reportedUserEntity; // 신고 당한 사용자

    @Column(nullable = false)
    private String contents; // 신고 내용

    public UserReport(UserEntity reportingUserEntity, UserEntity reportedUserEntity, String contents) {
        this.reportingUserEntity = reportingUserEntity;
        this.reportedUserEntity = reportedUserEntity;
        this.contents = contents;
    }
}
