package yumyum.demo.src.user.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import yumyum.demo.config.BaseException;
import yumyum.demo.config.BaseResponse;
import yumyum.demo.jwt.JwtFilter;
import yumyum.demo.jwt.TokenProvider;
import yumyum.demo.src.user.dto.*;
import yumyum.demo.src.user.service.UserService;
import yumyum.demo.utils.SecurityUtil;

import javax.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static yumyum.demo.config.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    @ApiOperation(value = "회원 가입 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })

    @PostMapping("/signup")
    public BaseResponse<String> signup(@Valid @RequestBody SignUpDto signUpDto) {
        try {
            userService.signup(signUpDto);

            return new BaseResponse<>("회원가입 성공!");

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "로그인 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    @PostMapping("/login")
    public BaseResponse<TokenDto> login(@Valid @RequestBody LoginDto loginDto) {

        try {
            userService.checkUsername(loginDto.getUsername()); //아이디 존재여부 체크

            userService.checkPassword(loginDto.getUsername(), loginDto.getPassword()); //비밀번호 일치 체크

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.createToken(authentication);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

            return new BaseResponse<>(new TokenDto(jwt));

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "닉네임 중복 체크 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    @PostMapping("/nickname")
    public BaseResponse<String> checkNickName(@Valid @RequestBody NickNameDto nickNameDto) {
        try {
            userService.checkNickName(nickNameDto.getNickName());

            return new BaseResponse<>("닉네임 사용가능!");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "아이디 중복 체크 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    @PostMapping("/username")
    public BaseResponse<String> checkUsername(@Valid @RequestBody UsernameDto usernameDto) {
        try {
            userService.checkUsernameDuplicate(usernameDto.getUsername());

            return new BaseResponse<>("아이디 사용가능!");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "마이페이지 조회 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    @GetMapping("/details")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<MyPageDto> getMyPage() {

        try {
            Optional<String> currentUsername = SecurityUtil.getCurrentUsername();

            return new BaseResponse<>(userService.getMyPage(currentUsername.get()));

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }


    @ApiOperation(value = "프로필 조회 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    @GetMapping("/profiles")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<UserProfileDto> getUserProfile() {

        try {
            Optional<String> currentUsername = SecurityUtil.getCurrentUsername();

            return new BaseResponse<>(userService.getUserProfile(currentUsername.get()));

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
    public BaseResponse<String> updateUserProfile(@Valid @RequestBody UserProfileDto userProfileDto) {

        try {
            Optional<String> currentUsername = SecurityUtil.getCurrentUsername();

            userService.updateUserProfile(currentUsername.get(), userProfileDto);

            return new BaseResponse<>("프로필 변경 완료!");

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
//    @ApiOperation(value = "내가 하트 찜한 음식점 조회 API")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
//            @ApiResponse(code = 3035, message = "중복된 닉네임입니다."),
//            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
//            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
//    })
//    @PatchMapping("/restaurants")
//    @PreAuthorize("hasAnyRole('USER')")
//    public BaseResponse<List<HeartRestaurantDto>> getHeartRestaurant() {
//        try {
//            Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
//
//            List<HeartRestaurantDto> result = userService.getHeartRestaurant(currentUsername.get());
//
//            return new BaseResponse<>(result);
//
//        } catch (BaseException e) {
//            return new BaseResponse<>(e.getStatus());
//        }
//    }



}
