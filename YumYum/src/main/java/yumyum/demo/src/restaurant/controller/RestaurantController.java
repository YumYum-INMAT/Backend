package yumyum.demo.src.restaurant.controller;

import static yumyum.demo.config.BaseResponseStatus.NOT_ACTIVATED_USER;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;
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
    @PreAuthorize("hasAnyRole('USER', 'GUEST')")
    public BaseResponse<GetRestaurantsDto> getRestaurants(@RequestParam(value = "sort", defaultValue = "1") int sortType) {
        try {
            String currentUserId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));
            Long userId = Long.parseLong(currentUserId);

            return new BaseResponse<>(restaurantService.getRestaurants(userId, sortType));
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
            String currentUserId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));
            Long userId = Long.parseLong(currentUserId);

            restaurantService.addRestaurantHeart(userId, restaurantId);

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
            String currentUserId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));
            Long userId = Long.parseLong(currentUserId);

            restaurantService.updateRestaurantHeart(userId, restaurantId);

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
    @PreAuthorize("hasAnyRole('USER', 'GUEST')")
    public BaseResponse<GetRestaurantDetailDto> getRestaurantDetails(@PathVariable("restaurantId") Long restaurantId) {
        try {
            String currentUserId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));
            Long userId = Long.parseLong(currentUserId);

            return new BaseResponse<>(restaurantService.getRestaurantDetails(userId, restaurantId));
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
            String currentUserId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));
            Long userId = Long.parseLong(currentUserId);

            restaurantService.createReview(userId, restaurantId, createReviewDto);

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
    @PreAuthorize("hasAnyRole('USER', 'GUEST')")
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
    @GetMapping("/search/result")
    @PreAuthorize("hasAnyRole('USER', 'GUEST')")
    public BaseResponse<List<RestaurantDto>> getSearchResult(@RequestParam(value = "query") String query, @RequestParam(value = "sort", defaultValue = "1")Integer sort){
        try {
            String currentUserId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));
            Long userId = Long.parseLong(currentUserId);

            return new BaseResponse<>(restaurantService.getSearchResult(userId,query, sort));
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "리뷰 리스트 조회 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    @GetMapping("/{restaurantId}/reviews")
    @PreAuthorize("hasAnyRole('USER', 'GUEST')")
    public BaseResponse<List<GetReviewDto>> getReviews(@PathVariable("restaurantId") Long restaurantId) {
        try {
            String currentUserId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));
            Long userId = Long.parseLong(currentUserId);

            return new BaseResponse<>(restaurantService.getReviews(userId, restaurantId));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "리뷰 신고 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    @PostMapping("/{reviewId}/report")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> reviewReport(@PathVariable("reviewId") Long reviewId,
                                                         @Valid @RequestBody ReviewReportDto reviewReportDto) {
        try {
            String currentUserId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));
            Long userId = Long.parseLong(currentUserId);

            restaurantService.reviewReport(userId, reviewId, reviewReportDto.getContents());

            return new BaseResponse<>("리뷰 신고 성공!");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
