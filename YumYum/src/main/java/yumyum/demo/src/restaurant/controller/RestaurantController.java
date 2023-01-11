package yumyum.demo.src.restaurant.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yumyum.demo.config.BaseException;
import yumyum.demo.config.BaseResponse;
import yumyum.demo.src.restaurant.dto.*;
import yumyum.demo.src.restaurant.service.RestaurantService;
import yumyum.demo.utils.SecurityUtil;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @ApiOperation(value = "홈화면 조회 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    @GetMapping("")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<GetRestaurantsDto> getRestaurants(@RequestParam(value = "sort", defaultValue = "1") int sortType) {
        try {
            Optional<String> currentUsername = SecurityUtil.getCurrentUsername();

            return new BaseResponse<>(restaurantService.getRestaurants(currentUsername.get(), sortType));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "음식점 추가 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    @PostMapping("")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public BaseResponse<String> createRestaurant(@Valid @RequestBody CreateRestaurantDto createRestaurantDto) {

        try {
            restaurantService.createRestaurant(createRestaurantDto);

            return new BaseResponse<>("프로필 변경 완료!");

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "음식점 하트 찜 설정 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    @PostMapping("/{restaurantId}/like")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> addRestaurantHeart(@PathVariable("restaurantId") Long restaurantId) {
        try {
            Optional<String> currentUsername = SecurityUtil.getCurrentUsername();

            restaurantService.addRestaurantHeart(currentUsername.get(), restaurantId);

            return new BaseResponse<>("음식점 좋아요 설정 완료!");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "음식점 하트 찜 해제 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    @PatchMapping("/{restaurantId}/like")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> updateRestaurantHeart(@PathVariable("restaurantId") Long restaurantId) {
        try {
            Optional<String> currentUsername = SecurityUtil.getCurrentUsername();

            restaurantService.updateRestaurantHeart(currentUsername.get(), restaurantId);

            return new BaseResponse<>("음식점 좋아요 해제 완료!");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "음식점 상세 조회 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    @GetMapping("/{restaurantId}")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<GetRestaurantDetailDto> getRestaurantDetails(@PathVariable("restaurantId") Long restaurantId) {
        try {
            Optional<String> currentUsername = SecurityUtil.getCurrentUsername();

            return new BaseResponse<>(restaurantService.getRestaurantDetails(currentUsername.get(), restaurantId));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "음식점 리뷰 작성 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    @PostMapping("/{restaurantId}/reviews")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> createReview(@PathVariable("restaurantId") Long restaurantId,
                                             @Valid @RequestBody CreateReviewDto createReviewDto) {
        try {
            Optional<String> currentUsername = SecurityUtil.getCurrentUsername();

            restaurantService.createReview(currentUsername.get(), restaurantId, createReviewDto);

            return new BaseResponse<>("음식점 리뷰 작성 완료!");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "음식점 검색창 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<List<PopularSearchWordDto>> getSearchWindow(){
        try {
            return new BaseResponse<>(restaurantService.getSearchWindow());
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "음식점 검색창 결과 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<List<RestaurantDto>> getSearchResult(@RequestParam(value = "query")String query, @RequestParam(value = "sort", defaultValue = "1")Integer sort){
        try {
            Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
            return new BaseResponse<>(restaurantService.getSearchResult(currentUsername.get(),query, sort));
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

//    @ApiOperation(value = "리뷰 리스트 조회 API")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
//            @ApiResponse(code = 400, message = "Bad Request"),
//            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
//            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
//    })
//    @GetMapping("/{restaurantId}/reviews")
//    @PreAuthorize("hasAnyRole('USER')")
//    public BaseResponse<List<>> getReviews(@PathVariable("restaurantId") Long restaurantId) {
//        try {
//            Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
//
//            return new BaseResponse<>(restaurantService.getReviews(currentUsername.get(), restaurantId));
//        } catch (BaseException e) {
//            return new BaseResponse<>(e.getStatus());
//        }
//    }
}
