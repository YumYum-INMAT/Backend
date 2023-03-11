package yumyum.demo.src.restaurant.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import yumyum.demo.config.BaseEntity;
import yumyum.demo.src.user.entity.UserEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "review")
@NoArgsConstructor
@DynamicInsert
public class ReviewEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private RestaurantEntity restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(nullable = false, columnDefinition = "int default 1")
    private Integer ratingStar;

    @Column(nullable = false, length = 200)
    private String contents;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewImgEntity> reviewImgEntities = new ArrayList<>();

    public ReviewEntity(UserEntity user, Integer ratingStar, String contents) {
        this.user = user;
        this.ratingStar = ratingStar;
        this.contents = contents;
    }

    public void addReviewImg(ReviewImgEntity reviewImgEntity) {
        reviewImgEntities.add(reviewImgEntity);
        reviewImgEntity.setReview(this);
    }
}
