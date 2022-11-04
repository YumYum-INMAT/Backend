package yumyum.demo.src.restaurant.service;

import static yumyum.demo.config.BaseResponseStatus.DUPLICATED_HEART;
import static yumyum.demo.config.BaseResponseStatus.DUPLICATED_NICKNAME;
import static yumyum.demo.config.BaseResponseStatus.DUPLICATED_USERNAME;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yumyum.demo.config.BaseException;
import yumyum.demo.src.restaurant.dto.CreateRestaurantDto;
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
        Optional<UserEntity> userEntityByUsername = userRepository.findUserEntityByUsername(username);

        Optional<RestaurantEntity> restaurantEntityById = restaurantRepository.findRestaurantEntityById(restaurantId);

        Optional<HeartEntity> heartEntityByRestaurantAndUser = heartRepository.findHeartEntityByRestaurantAndUser(
                restaurantEntityById.get(), userEntityByUsername.get());

        if(heartEntityByRestaurantAndUser.isPresent()) {
            throw new BaseException(DUPLICATED_HEART);
        }

        HeartEntity heartEntity = new HeartEntity(restaurantEntityById.get(), userEntityByUsername.get());

        heartRepository.save(heartEntity);
    }
}
