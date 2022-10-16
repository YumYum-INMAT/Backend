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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import yumyum.demo.config.BaseException;
import yumyum.demo.config.BaseResponse;
import yumyum.demo.jwt.JwtFilter;
import yumyum.demo.jwt.TokenProvider;
import yumyum.demo.src.user.dto.LoginDto;
import yumyum.demo.src.user.dto.TokenDto;
import yumyum.demo.src.user.dto.UserDto;
import yumyum.demo.src.user.entity.UserEntity;
import yumyum.demo.src.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.regex.Pattern;

import static yumyum.demo.config.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/signup")
    public BaseResponse<UserEntity> signup(@Valid @RequestBody UserDto userDto) {
        try {
            return new BaseResponse<>(userService.signup(userDto));
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
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })

    @PostMapping("/login")
    public BaseResponse<TokenDto> login(@Valid @RequestBody LoginDto loginDto) {

        /**
         * 형식적 Validation 처리
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

    @GetMapping("/details")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public BaseResponse<UserEntity> getMyUserInfo(HttpServletRequest request) {

        try {
            return new BaseResponse<>(userService.getMyUserWithAuthorities().get());
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
