package yumyum.demo.src.restaurant.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import yumyum.demo.config.BaseEntity;

@Entity
@Getter
@Table(name = "todayRecommend")
@NoArgsConstructor
@DynamicInsert
public class TodayRecommendEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "today_recommend_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String foodName;

    @Column(nullable = false)
    private String imgUrl;
}
