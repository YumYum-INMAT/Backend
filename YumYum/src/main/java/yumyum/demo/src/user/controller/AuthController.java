package yumyum.demo.src.user.controller;

import static yumyum.demo.config.BaseResponseStatus.EMPTY_ACCESS_TOKEN;
import static yumyum.demo.config.BaseResponseStatus.EMPTY_REFRESH_TOKEN;
import static yumyum.demo.config.BaseResponseStatus.INVALID_ACCESS_TOKEN;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yumyum.demo.config.BaseException;
import yumyum.demo.config.BaseResponse;
import yumyum.demo.jwt.TokenProvider;
import yumyum.demo.src.user.dto.LoginDto;
import yumyum.demo.src.user.dto.NickNameDto;
import yumyum.demo.src.user.dto.SignUpDto;
import yumyum.demo.src.user.dto.TokenDto;
import yumyum.demo.src.user.dto.UsernameDto;
import yumyum.demo.src.user.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class AuthController {
    private final AuthService authService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public static final String ACCESS_TOKEN = "ACCESS-TOKEN";
    public static final String REFRESH_TOKEN = "REFRESH-TOKEN";

    @ApiOperation(value = "회원 가입 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @PostMapping("/signup")
    public BaseResponse<String> signup(@Valid @RequestBody SignUpDto signUpDto) {
        try {
            authService.signup(signUpDto);

            return new BaseResponse<>("회원가입 성공!");

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "닉네임 중복 체크 API (회원 가입 시 사용)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @PostMapping("/nickname")
    public BaseResponse<String> checkNickName(@Valid @RequestBody NickNameDto nickNameDto) {
        try {
            authService.checkNickName(nickNameDto.getNickName());

            return new BaseResponse<>("닉네임 사용가능!");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "아이디 중복 체크 API (회원 가입 시 사용)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @PostMapping("/username")
    public BaseResponse<String> checkUsername(@Valid @RequestBody UsernameDto usernameDto) {
        try {
            authService.checkUsernameDuplicate(usernameDto.getUsername());

            return new BaseResponse<>("아이디 사용가능!");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "로그인 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @PostMapping("/login")
    public BaseResponse<TokenDto> login(@RequestHeader("User-Agent") String userAgent,
                                        @Valid @RequestBody LoginDto loginDto) {

        try {
            authService.checkUsername(loginDto.getUsername()); //아이디 존재여부 체크

            authService.checkPassword(loginDto.getUsername(), loginDto.getPassword()); //비밀번호 일치 체크

            Authentication authentication = getAuthentication(loginDto.getUsername(), loginDto.getPassword());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = tokenProvider.createAccessToken(authentication);
            String refreshToken = tokenProvider.createRefreshToken(authentication);

            //발급받은 리프레쉬 토큰을 디비에 저장
            authService.updateRefreshToken(loginDto.getUsername(), refreshToken, userAgent);

            return new BaseResponse<>(new TokenDto(accessToken, refreshToken));

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "비회원 로그인 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @PostMapping("/login-anonymous")
    public BaseResponse<TokenDto> anonymousLogin(@RequestHeader("User-Agent") String userAgent) {
        try {
            LoginDto anonymousLoginDto = authService.anonymousLogin();

            authService.checkUsername(anonymousLoginDto.getUsername()); //아이디 존재여부 체크

            Authentication authentication = getAuthentication(anonymousLoginDto.getUsername(),
                    anonymousLoginDto.getPassword());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = tokenProvider.createAccessToken(authentication);
            String refreshToken = tokenProvider.createRefreshToken(authentication);
            authService.updateRefreshToken(anonymousLoginDto.getUsername(), refreshToken, userAgent);

            return new BaseResponse<>(new TokenDto(accessToken, refreshToken));

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "access 토큰 재발급 API (access 토큰 만료시)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @PostMapping("/issue")
    public BaseResponse<TokenDto> reissueAccessToken(
            HttpServletRequest request,
            @RequestHeader("User-Agent") String userAgent) {
        String accessToken = tokenProvider.resolveAccessToken(request);
        String refreshToken = request.getHeader(REFRESH_TOKEN);

        if (!StringUtils.hasText(accessToken)) {
            return new BaseResponse<>(EMPTY_ACCESS_TOKEN);
        }

        //access 토큰 만료가 아닌 경우 -> 잘못된 access 토큰으로 간주
        if (!tokenProvider.isExpiredAccessToken(accessToken)) {
            return new BaseResponse<>(INVALID_ACCESS_TOKEN);
        }

        if (!StringUtils.hasText(refreshToken)) {
            return new BaseResponse<>(EMPTY_REFRESH_TOKEN);
        }

        try {
            return new BaseResponse<>(authService.reissueAccessToken(refreshToken, userAgent));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    private Authentication getAuthentication(String username, String password) {
        return authenticationManagerBuilder.getObject()
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

}
