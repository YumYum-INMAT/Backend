package yumyum.demo.src.restaurant.repository;

import static yumyum.demo.src.restaurant.entity.QHeartEntity.heartEntity;
import static yumyum.demo.src.restaurant.entity.QRestaurantEntity.restaurantEntity;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import yumyum.demo.config.Status;
import yumyum.demo.src.restaurant.dto.RestaurantDto;
import yumyum.demo.src.restaurant.entity.HeartEntity;
import yumyum.demo.src.restaurant.entity.QRestaurantEntity;
import yumyum.demo.src.restaurant.entity.RestaurantEntity;

@Repository
@RequiredArgsConstructor
public class RestaurantDynamicQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;


    public List<RestaurantDto> findRestaurantsOrderByCreatedAt(Long userId, int sortType) {

        List<Long> heartRestaurantList = getHeartRestaurantsId(userId);

        return jpaQueryFactory
                .select(Projections.fields(RestaurantDto.class,
                        restaurantEntity.id.as("restaurantId"),
                        restaurantEntity.profileImgUrl.as("profileImgUrl"),
                        restaurantEntity.restaurantName.as("restaurantName"),
                        restaurantEntity.address.as("address"),
                        restaurantEntity.averageStar.as("averageStar"),
                        restaurantEntity.countReview.as("countReview"),
                        restaurantEntity.averagePrice.as("averagePrice"),
                        restaurantEntity.complexity.as("complexity"),
                        restaurantEntity.restaurantType.as("restaurantType"),
                        new CaseBuilder().when(restaurantEntity.id.in(heartRestaurantList)).then(true)
                                .otherwise(false).as("userHeart")))
                .from(restaurantEntity)
                .where(eqStatus())
                .orderBy(sortCondition(sortType))
                .limit(50)
                .fetch();
    }

    public List<Long> getHeartRestaurantsId(Long userId) {
        return jpaQueryFactory
                .select(heartEntity.restaurant.id)
                .from(heartEntity)
                .where(heartEntity.user.id.eq(userId)).fetch();
    }

    private BooleanExpression eqStatus() {
        return restaurantEntity.status.eq(Status.ACTIVE);
    }

    private OrderSpecifier sortCondition(int sortType) {

        //sortType = 1 -> 평점순
         if (sortType == 1) {
             return restaurantEntity.averageStar.desc();
         }
        //sortType = 2 -> 평균 가격 낮은순
        if (sortType == 2) {
            return restaurantEntity.averagePrice.asc();
        }
        //sortType = 3 -> 평균 가격 높은순
        if (sortType == 3) {
            return restaurantEntity.averagePrice.desc();
        }
        //sortType = 4 -> 리뷰 많은순
        if (sortType == 4) {
            return restaurantEntity.countReview.desc();
        }
        //sortType = 5 -> 하트찜 많은순
        if (sortType == 5) {
            return restaurantEntity.countHeart.desc();
        }
        return null;
    }
}
