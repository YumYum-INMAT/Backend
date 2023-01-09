package yumyum.demo.src.restaurant.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yumyum.demo.config.BaseException;
import yumyum.demo.config.Status;
import yumyum.demo.src.restaurant.dto.*;
import yumyum.demo.src.restaurant.entity.BannerEntity;
import yumyum.demo.src.restaurant.entity.CategoryEntity;
import yumyum.demo.src.restaurant.entity.HeartEntity;
import yumyum.demo.src.restaurant.entity.RestaurantEntity;
import yumyum.demo.src.restaurant.entity.RestaurantImgEntity;
import yumyum.demo.src.restaurant.entity.RestaurantMenuEntity;
import yumyum.demo.src.restaurant.entity.ReviewEntity;
import yumyum.demo.src.restaurant.entity.TodayRecommendEntity;
import yumyum.demo.src.restaurant.repository.*;
import yumyum.demo.src.user.entity.UserEntity;
import yumyum.demo.src.user.repository.UserRepository;

import static yumyum.demo.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantDynamicQueryRepository restaurantDynamicQueryRepository;
    private final CategoryRepository categoryRepository;
    private final HeartRepository heartRepository;
    private final BannerRepository bannerRepository;
    private final ReviewRepository reviewRepository;
    private final TodayRecommendRepository todayRecommendRepository;
    private final UserRepository userRepository;

    private final RestaurantJdbcTempRepository restaurantJdbcTempRepository;

    public void createRestaurant(CreateRestaurantDto createRestaurantDto) throws BaseException {

        //createRestaurantDto로 부터 받은 변수중 메뉴 리스트를 저장하기 위한 리스트 선언
        List<RestaurantMenuEntity> restaurantMenuEntityList = new ArrayList<>();

        for (RestaurantMenuDto restaurantMenuDto : createRestaurantDto.getRestaurantMenuList()) {
            Optional<CategoryEntity> categoryEntity = categoryRepository.findCategoryEntityById(
                    restaurantMenuDto.getCategoryId());

            RestaurantMenuEntity restaurantMenuEntity = RestaurantMenuEntity.builder()
                    .category(categoryEntity.get())
                    .menuName(restaurantMenuDto.getMenuName())
                    .price(restaurantMenuDto.getPrice())
                    .build();

            restaurantMenuEntityList.add(restaurantMenuEntity);
        }


        RestaurantEntity restaurantEntity = new RestaurantEntity(
                createRestaurantDto.getRestaurantName(),
                createRestaurantDto.getImgUrl(),
                createRestaurantDto.getContactNumber(),
                createRestaurantDto.getAddress(),
                createRestaurantDto.getLatitude(),
                createRestaurantDto.getLongitude(),
                createRestaurantDto.getRestaurantType(),
                restaurantMenuEntityList);

        restaurantRepository.save(restaurantEntity);
    }

    public void addRestaurantHeart(String username, Long restaurantId) throws BaseException {
        UserEntity userEntityByUsername = userRepository.findUserEntityByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        RestaurantEntity restaurantEntityById = restaurantRepository.findRestaurantEntityByIdAndStatus(restaurantId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_RESTAURANT));

        Optional<HeartEntity> heartEntityByRestaurantAndUser = heartRepository.findHeartEntityByRestaurantAndUser(
                restaurantEntityById, userEntityByUsername);

        if(heartEntityByRestaurantAndUser.isPresent()) {
            Status status = heartEntityByRestaurantAndUser.get().getStatus();

            //만약 좋아요가 있다면 status 값이 ACTIVE 인 경우
            if(status.equals(Status.ACTIVE)) {
                throw new BaseException(DUPLICATED_HEART);
            }
            //만약 좋아요 취소기록이 있다면, status 값이 0인 경우 다시 1로 활성화
            if(status.equals(Status.INACTIVE)) {
                heartEntityByRestaurantAndUser.get().setStatus(Status.ACTIVE);
                heartRepository.save(heartEntityByRestaurantAndUser.get());

                restaurantEntityById.increaseCountHeart();
                restaurantRepository.save(restaurantEntityById);
                return;
            }
        }
        HeartEntity heartEntity = new HeartEntity(restaurantEntityById, userEntityByUsername);
        heartRepository.save(heartEntity);
        restaurantEntityById.increaseCountHeart();
        restaurantRepository.save(restaurantEntityById);
    }

    public void updateRestaurantHeart(String username, Long restaurantId) throws BaseException {
        UserEntity userEntityByUsername = userRepository.findUserEntityByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        RestaurantEntity restaurantEntityById = restaurantRepository.findRestaurantEntityByIdAndStatus(restaurantId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_RESTAURANT));

        HeartEntity heartEntityByRestaurantAndUser = heartRepository.findHeartEntityByRestaurantAndUser(restaurantEntityById, userEntityByUsername)
                .orElseThrow(() -> new BaseException(FAIL_TO_FIND_HEART));

        //이미 좋아요가 취소된 상태인 경우
        if(heartEntityByRestaurantAndUser.getStatus().equals(Status.INACTIVE)) {
            throw new BaseException(ALREADY_HEART_CANCEL);
        }
        heartEntityByRestaurantAndUser.setStatus(Status.INACTIVE);
        heartRepository.save(heartEntityByRestaurantAndUser);
        restaurantEntityById.decreaseCountHeart();
        restaurantRepository.save(restaurantEntityById);
    }

    public GetRestaurantsDto getRestaurants(String username, int sortType) {
        UserEntity userEntity = userRepository.findUserEntityByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        Long userId = userEntity.getId();

        List<BannerEntity> bannerEntities = bannerRepository.findAllByStatus(Status.ACTIVE);
        List<BannerDto> bannerList = new ArrayList<>();
        for (BannerEntity entity : bannerEntities) {
            bannerList.add(new BannerDto(entity.getBannerUrl()));
        }

        List<TodayRecommendEntity> todayRecommendEntities = todayRecommendRepository.findTop3ByStatus();
        List<TodayRecommendDto> todayRecommendList = new ArrayList<>();
        for (TodayRecommendEntity entity : todayRecommendEntities) {
            todayRecommendList.add(new TodayRecommendDto(entity.getId(), entity.getImgUrl(), entity.getFoodName()));
        }

        List<ReviewEntity> reviewEntities = reviewRepository.findTop3ByStatusOrderByCreatedAtDesc(Status.ACTIVE);
        List<RecentReviewDto> recentReviewList = new ArrayList<>();
        for (ReviewEntity entity : reviewEntities) {
            recentReviewList.add(new RecentReviewDto(
                    entity.getId(),
                    entity.getImgUrl(),
                    entity.getRestaurant().getRestaurantName(),
                    entity.getUser().getNickName(),
                    entity.getRatingStar(),
                    entity.getContents()));
        }

        List<RestaurantDto> restaurantList = restaurantDynamicQueryRepository.findRestaurantsOrderByCreatedAt(userId, sortType);

        //sortType = 1 -> 평점순(평점이 같으면 리뷰 많은게 먼저)
        //sortType = 2 -> 평균 가격 낮은순
        //sortType = 3 -> 평균 가격 높은순
        //sortType = 4 -> 리뷰 많은순
        //sortType = 5 -> 하트찜 많은순

        return new GetRestaurantsDto(bannerList, todayRecommendList, recentReviewList, restaurantList);
    }

    public GetRestaurantDetailDto getRestaurantDetails(String username, Long restaurantId) throws BaseException {
        UserEntity userEntity = userRepository.findUserEntityByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        Long userId = userEntity.getId();

        RestaurantEntity restaurantEntity = restaurantRepository.findRestaurantEntityByIdAndStatus(restaurantId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_RESTAURANT));

        List<String> restaurantImgList = new ArrayList<>();
        for (RestaurantImgEntity restaurantImgEntity : restaurantEntity.getRestaurantImgEntities()) {
            if (restaurantImgList.size() == 5) {
                break;
            }
            restaurantImgList.add(restaurantImgEntity.getImgUrl());
        }

        List<RestaurantMenuDto> menuList = new ArrayList<>();
        for (RestaurantMenuEntity restaurantMenuEntity : restaurantEntity.getRestaurantMenuEntities()) {
            menuList.add(new RestaurantMenuDto(restaurantMenuEntity.getId(), restaurantMenuEntity.getMenuName(), restaurantMenuEntity.getPrice()));
        }

        Optional<HeartEntity> heartEntity = heartRepository.findHeartEntityByRestaurantAndUser(restaurantEntity, userEntity);
        boolean userHeart = false;
        if (heartEntity.isPresent()) {
            if (heartEntity.get().getStatus().equals(Status.ACTIVE)) {
                userHeart = true;
            }
        }

        List<GetReviewDto> reviewList = new ArrayList<>();
        for (ReviewEntity reviewEntity : restaurantEntity.getReviewEntities()) {
            if (reviewList.size() == 5) {
                break;
            }
            reviewList.add(new GetReviewDto(
                    reviewEntity.getId(),
                    reviewEntity.getImgUrl(),
                    reviewEntity.getUser().getNickName(),
                    reviewEntity.getRatingStar(),
                    reviewEntity.getContents()));
        }

        return new GetRestaurantDetailDto(
                restaurantId,
                restaurantEntity.getProfileImgUrl(),
                restaurantEntity.getRestaurantName(),
                restaurantImgList,
                menuList,
                restaurantEntity.getAddress(),
                userHeart,
                restaurantEntity.getContactNumber(),
                restaurantEntity.getAverageStar(),
                restaurantEntity.getCountReview(),
                restaurantEntity.getCountHeart(),
                restaurantEntity.getAveragePrice(),
                restaurantEntity.getComplexity(),
                restaurantEntity.getRestaurantType(),
                reviewList);
    }

    public void createReview(String username, Long restaurantId, CreateReviewDto createReviewDto) {
        UserEntity userEntity = userRepository.findUserEntityByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        RestaurantEntity restaurantEntity = restaurantRepository.findRestaurantEntityByIdAndStatus(restaurantId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_RESTAURANT));

        ReviewEntity reviewEntity = new ReviewEntity(
                userEntity,
                createReviewDto.getRatingStar(),
                createReviewDto.getContents());
        if (createReviewDto.getImgUrl() == null) {
            reviewEntity.setImgUrl(null);
        }
        else {
            reviewEntity.setImgUrl(createReviewDto.getImgUrl());
        }

        restaurantEntity.addReview(reviewEntity);

        restaurantRepository.save(restaurantEntity);
    }

    public List<PopularSearchWordDto> getSearchWindow(String contents) {
        restaurantJdbcTempRepository.postSearch(contents);
        return restaurantJdbcTempRepository.getPopularSearchWord();

    }

    public List<SearchRestaurantDto> getSearchResult(String search, Long sort) throws BaseException{
       if(sort == 1){
           return restaurantJdbcTempRepository.getSearchResult1(search);
       }
       else if(sort == 2){
           return restaurantJdbcTempRepository.getSearchRestul2(search);
       }
       else if(sort == 3){
           return restaurantJdbcTempRepository.getSearchRestul3(search);
       }
       else if(sort == 4){
           return restaurantJdbcTempRepository.getSearchRestul4(search);
       }
       else if(sort == 5){
           return restaurantJdbcTempRepository.getSearchRestul5(search);
       }
       else{
           throw new BaseException(DATABASE_ERROR);
       }
    }
}