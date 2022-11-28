package yumyum.demo.src.restaurant.repository;

import static yumyum.demo.src.restaurant.entity.QHeartEntity.heartEntity;
import static yumyum.demo.src.restaurant.entity.QRestaurantEntity.restaurantEntity;

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


    public List<RestaurantDto> findRestaurantsOrderByCreatedAt(Long userId) {

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
                .fetch();
    }

    public List<Long> getHeartRestaurantsId(Long userId) {
        return jpaQueryFactory
                .select(heartEntity.restaurant.id)
                .from(heartEntity)
                .where(heartEntity.user.id.eq(userId)).fetch();
    }

    private BooleanExpression eqStatus () {
        return restaurantEntity.status.eq(Status.ACTIVE);
    }
}
