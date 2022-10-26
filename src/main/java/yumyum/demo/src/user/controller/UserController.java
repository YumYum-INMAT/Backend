package yumyum.demo.src.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import yumyum.demo.config.BaseException;
import yumyum.demo.config.BaseResponse;
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

    @PostMapping("/signup")
    public BaseResponse<UserEntity> signup(@Valid @RequestBody UserDto userDto) {
        try {
            return new BaseResponse<>(userService.signup(userDto));
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
