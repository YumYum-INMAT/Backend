package yumyum.demo.src.restaurant.service;

import static yumyum.demo.config.BaseResponseStatus.ALREADY_HEART_CANCEL;
import static yumyum.demo.config.BaseResponseStatus.DUPLICATED_HEART;
import static yumyum.demo.config.BaseResponseStatus.DUPLICATED_NICKNAME;
import static yumyum.demo.config.BaseResponseStatus.DUPLICATED_USERNAME;
import static yumyum.demo.config.BaseResponseStatus.FAIL_TO_FIND_HEART;
import static yumyum.demo.config.BaseResponseStatus.NOT_ACTIVATED_RESTAURANT;
import static yumyum.demo.config.BaseResponseStatus.NOT_ACTIVATED_USER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yumyum.demo.config.BaseException;
import yumyum.demo.config.Status;
import yumyum.demo.src.restaurant.dto.CreateRestaurantDto;
import yumyum.demo.src.restaurant.dto.GetRestaurantsDto;
import yumyum.demo.src.restaurant.dto.RestaurantMenuDto;
import yumyum.demo.src.restaurant.entity.CategoryEntity;
import yumyum.demo.src.restaurant.entity.HeartEntity;
import yumyum.demo.src.restaurant.entity.RestaurantEntity;
import yumyum.demo.src.restaurant.entity.RestaurantMenuEntity;
import yumyum.demo.src.restaurant.repository.CategoryRepository;
import yumyum.demo.src.restaurant.repository.HeartRepository;
import yumyum.demo.src.restaurant.repository.RestaurantRepository;
import yumyum.demo.src.user.entity.Authority;
import yumyum.demo.src.user.entity.UserEntity;
import yumyum.demo.src.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final HeartRepository heartRepository;
    private final UserRepository userRepository;

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

    public GetRestaurantsDto getRestaurants(String username) {
        Optional<UserEntity> userEntity = userRepository.findUserEntityByUsername(username);
        Long userId = userEntity.get().getId();


        return null;
    }
}
