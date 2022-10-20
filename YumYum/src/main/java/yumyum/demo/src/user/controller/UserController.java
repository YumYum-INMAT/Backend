package yumyum.demo.src.user.controller;

import antlr.Token;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import yumyum.demo.config.BaseException;
import yumyum.demo.config.BaseResponse;
import yumyum.demo.jwt.JwtFilter;
import yumyum.demo.jwt.TokenProvider;
import yumyum.demo.src.user.dto.*;
import yumyum.demo.src.user.entity.UserEntity;
import yumyum.demo.src.user.service.UserService;
import yumyum.demo.utils.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
            @ApiResponse(code = 2020, message = "이메일을 입력해주세요."),
            @ApiResponse(code = 2021, message = "잘못된 이메일 형식입니다."),
            @ApiResponse(code = 2030, message = "비밀 번호를 입력해주세요."),
            @ApiResponse(code = 2031, message = "비밀 번호는 특수문자 포함 8자 이상 20자리 이하입니다."),
            @ApiResponse(code = 3010, message = "없는 아이디이거나 비밀번호가 틀렸습니다."),
            @ApiResponse(code = 400, message = "Bad Request")
    })

    @PostMapping("/signup")
    public BaseResponse<String> signup(@Valid @RequestBody SignUpDto signUpDto) {
        /**
         * 형식적 Validation 처리, 요청받을때 @Valid로 체크하지만 한번 더 체크해줌.
         */

        if(signUpDto.getEmail() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }

        if(signUpDto.getEmail().length() > 320) {
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }

        String emailPattern = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-z]+$";
        if(!Pattern.matches(emailPattern, signUpDto.getEmail())) {
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }

        if(signUpDto.getPassword() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }

        String passwordPattern = "^(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,20}";
        if(!Pattern.matches(passwordPattern, signUpDto.getPassword())) {
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
        }

        if(signUpDto.getNickName() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_NICKNAME);
        }

        String nickNamePattern = "[가-힣]{2,8}";
        if(!Pattern.matches(nickNamePattern, signUpDto.getNickName())) {
            return new BaseResponse<>(POST_USERS_INVALID_NICKNAME);
        }

        if(signUpDto.getAge() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_AGE);
        }

        if(signUpDto.getAge() < 1 || signUpDto.getAge() > 100) {
            return new BaseResponse<>(POST_USERS_INVALID_AGE);
        }

        if(signUpDto.getGender() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_GENDER);
        }

        if(signUpDto.getGender() != 'F' && signUpDto.getGender() != 'M') {
            return new BaseResponse<>(POST_USERS_INVALID_GENDER);
        }

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
            @ApiResponse(code = 2020, message = "이메일을 입력해주세요."),
            @ApiResponse(code = 2021, message = "잘못된 이메일 형식입니다."),
            @ApiResponse(code = 2030, message = "비밀 번호를 입력해주세요."),
            @ApiResponse(code = 2031, message = "비밀 번호는 특수문자 포함 8자 이상 20자리 이하입니다."),
            @ApiResponse(code = 3010, message = "없는 아이디이거나 비밀번호가 틀렸습니다."),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @PostMapping("/login")
    public BaseResponse<TokenDto> login(@Valid @RequestBody LoginDto loginDto) {

        /**
         * 형식적 Validation 처리, 요청받을때 @Valid로 체크하지만 한번 더 체크해줌.
         */

        if(loginDto.getEmail() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }

        if(loginDto.getEmail().length() > 320) {
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }

        String emailPattern = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-z]+$";
        if(!Pattern.matches(emailPattern, loginDto.getEmail())) {
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }

        if(loginDto.getPassword() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }

        String passwordPattern = "^(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,20}";
        if(!Pattern.matches(passwordPattern, loginDto.getPassword())) {
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
        }

        try {
            userService.checkEmail(loginDto.getEmail()); //이메일 존재여부 체크

            userService.checkPassword(loginDto.getEmail(), loginDto.getPassword()); //비밀번호 일치 체크

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

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
            @ApiResponse(code = 3035, message = "중복된 닉네임입니다."),
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

    @ApiOperation(value = "마이페이지 조회 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    @GetMapping("/details")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<MyPageDto> getMyPage() {

        try {
            Optional<String> currentEmail = SecurityUtil.getCurrentEmail();

            return new BaseResponse<>(userService.getMyPage(currentEmail.get()));

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "프로필 조회 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    @GetMapping("/profiles")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<UserProfileDto> getUserProfile() {

        try {
            Optional<String> currentEmail = SecurityUtil.getCurrentEmail();

            return new BaseResponse<>(userService.getUserProfile(currentEmail.get()));

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "프로필 수정 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 3035, message = "중복된 닉네임입니다."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    @PatchMapping("/profiles")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> updateUserProfile(@Valid @RequestBody UserProfileDto userProfileDto) {

        try {
            Optional<String> currentEmail = SecurityUtil.getCurrentEmail();

            userService.updateUserProfile(currentEmail.get(), userProfileDto);

            return new BaseResponse<>("프로필 변경 완료!");

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public BaseResponse<UserEntity> getUserInfo(@PathVariable("email") String email) {
        try {
            return new BaseResponse<>(userService.getUserWithAuthorities(email).get());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }


}
