package yumyum.demo.src.user.controller;

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

    @PostMapping("/login")
    public BaseResponse<TokenDto> login(@Valid @RequestBody LoginDto loginDto) {

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
