package yumyum.demo.src.user.controller;

import static yumyum.demo.config.BaseResponseStatus.NOT_ACTIVATED_USER;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import yumyum.demo.config.BaseException;
import yumyum.demo.config.BaseResponse;
import yumyum.demo.src.community.dto.CommunityMainDto;
import yumyum.demo.src.user.dto.*;
import yumyum.demo.src.user.service.UserService;
import yumyum.demo.utils.SecurityUtil;

import javax.validation.Valid;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @ApiOperation(value = "프로필 조회 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    @GetMapping("/profiles")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<GetUserProfileDto> getUserProfile() {
        try {
            String currentUserId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));
            Long userId = Long.parseLong(currentUserId);

            return new BaseResponse<>(userService.getUserProfile(userId));

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "프로필 수정 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    @PatchMapping("/profiles")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> updateUserProfile(@Valid @RequestBody UpdateUserProfileDto userProfileDto) {

        try {
            String currentUserId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));
            Long userId = Long.parseLong(currentUserId);

            userService.updateUserProfile(userId, userProfileDto);

            return new BaseResponse<>("프로필 변경 완료!");

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }


    @ApiOperation(value = "내가 쓴 게시글 조회 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    @GetMapping("/posts")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<List<CommunityMainDto>> getPost(){
        try{
            String currentUserId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));
            Long userId = Long.parseLong(currentUserId);

            return new BaseResponse<>(userService.getPost(userId));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "내가 쓴 리뷰 조회 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    @GetMapping("/reviews")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<List<MyReviewDto>> getMyReview(){
        try{
            String currentUserId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));
            Long userId = Long.parseLong(currentUserId);

            return new BaseResponse<>(userService.getMyReview(userId));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "내가 하트찜한 음식점 조회 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    @GetMapping("/restaurants")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<List<MyHeartRestaurantDto>> getMyHeartRestaurant(){
        try{
            String currentUserId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));
            Long userId = Long.parseLong(currentUserId);

            return new BaseResponse<>(userService.getMyHeartRestaurant(userId));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
