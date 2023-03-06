package yumyum.demo.src.user.service;

import static yumyum.demo.config.BaseResponseStatus.DUPLICATED_EMAIL;
import static yumyum.demo.config.BaseResponseStatus.DUPLICATED_NICKNAME;
import static yumyum.demo.config.BaseResponseStatus.DUPLICATED_USERNAME;
import static yumyum.demo.config.BaseResponseStatus.EXPIRED_REFRESH_TOKEN;
import static yumyum.demo.config.BaseResponseStatus.FAILED_TO_LOGIN;
import static yumyum.demo.config.BaseResponseStatus.INVALID_ACCESS_GOOGLE;
import static yumyum.demo.config.BaseResponseStatus.INVALID_REFRESH_TOKEN;
import static yumyum.demo.config.BaseResponseStatus.NOT_ACTIVATED_USER;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import yumyum.demo.config.BaseException;
import yumyum.demo.config.BaseResponseStatus;
import yumyum.demo.config.LogInType;
import yumyum.demo.config.Status;
import yumyum.demo.jwt.TokenProvider;
import yumyum.demo.src.user.dto.GoogleUser;
import yumyum.demo.src.user.dto.GuestLoginDto;
import yumyum.demo.src.user.dto.KakaoUser;
import yumyum.demo.src.user.dto.SignUpDto;
import yumyum.demo.src.user.dto.TokenDto;
import yumyum.demo.src.user.entity.Authority;
import yumyum.demo.src.user.entity.RefreshTokenEntity;
import yumyum.demo.src.user.entity.UserEntity;
import yumyum.demo.src.user.repository.RefreshTokenRepository;
import yumyum.demo.src.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Transactional
    public void signup(SignUpDto signUpDto) throws BaseException {
        //이메일 중복 체크
        if (userRepository.findUserEntityByEmail(signUpDto.getEmail()).isPresent()) {
            throw new BaseException(DUPLICATED_EMAIL);
        }

        //닉네임 중복 체크
        if(userRepository.findUserEntityByNickNameAndStatus(signUpDto.getNickName(), Status.ACTIVE).isPresent()) {
            throw new BaseException(DUPLICATED_NICKNAME);
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        UserEntity user = UserEntity.builder()
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .email(signUpDto.getEmail())
                .nickName(signUpDto.getNickName())
                .age(signUpDto.getAge())
                .gender(signUpDto.getGender())
                .authorities(Collections.singleton(authority))
                .logInType(LogInType.EMAIL)
                .snsId(null)
                .build();

        userRepository.save(user);
    }

    public Long getUserId(String email) throws BaseException {
        UserEntity user = userRepository.findUserEntityByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));
        return user.getId();
    }

    //아이디 존재 여부 체크 -> 로그인에서 사용
    public void checkEmail(String email) throws BaseException {
        if(userRepository.findUserEntityByEmail(email).isEmpty()) {
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    //아이디 존재 여부 체크 -> 로그인에서 사용
    public void checkEmailDuplicate(String email) throws BaseException {
        if(userRepository.findUserEntityByEmail(email).isPresent()) {
            throw new BaseException(DUPLICATED_EMAIL);
        }
    }

    public void checkPassword(String email, String password) throws BaseException {
        UserEntity user = userRepository.findUserEntityByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    public void checkNickName(String nickName) throws BaseException {
        if(userRepository.findUserEntityByNickNameAndStatus(nickName, Status.ACTIVE).isPresent()) {
            throw new BaseException(DUPLICATED_NICKNAME);
        }
    }

    public void updateRefreshToken(String email, String refreshToken, String userAgent, String deviceIdentifier) throws BaseException {
        UserEntity foundUserEntity = userRepository.findUserEntityByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenRepository.findRefreshTokenEntityByUserAndStatus(foundUserEntity, Status.ACTIVE);

        //첫 로그인인 경우 -> 디비에 저장된 refresh 토큰 칼럼이 없음
        if (refreshTokenEntity.isEmpty()) {
            RefreshTokenEntity createdRefreshTokenEntity = new RefreshTokenEntity(foundUserEntity, refreshToken, userAgent, deviceIdentifier);
            refreshTokenRepository.save(createdRefreshTokenEntity);
        }
        else {
            refreshTokenEntity.get().updateRefreshToken(refreshToken);
            refreshTokenEntity.get().updateDeviceIdentifier(deviceIdentifier);
            refreshTokenEntity.get().updateUserAgent(userAgent);
            refreshTokenRepository.save(refreshTokenEntity.get());
        }
    }

    public void updateSnsUserRefreshToken(String snsId, String refreshToken, String userAgent, String deviceIdentifier) throws BaseException {
        UserEntity foundUserEntity = userRepository.findUserEntityBySnsIdAndStatus(snsId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenRepository.findRefreshTokenEntityByUserAndStatus(foundUserEntity, Status.ACTIVE);

        //첫 로그인인 경우 -> 디비에 저장된 refresh 토큰 칼럼이 없음
        if (refreshTokenEntity.isEmpty()) {
            RefreshTokenEntity createdRefreshTokenEntity = new RefreshTokenEntity(foundUserEntity, refreshToken, userAgent, deviceIdentifier);
            refreshTokenRepository.save(createdRefreshTokenEntity);
        }
        else {
            refreshTokenEntity.get().updateRefreshToken(refreshToken);
            refreshTokenEntity.get().updateDeviceIdentifier(deviceIdentifier);
            refreshTokenEntity.get().updateUserAgent(userAgent);
            refreshTokenRepository.save(refreshTokenEntity.get());
        }
    }

    public GuestLoginDto guestLogin() throws BaseException {
        List<UserEntity> guestUserEntities = userRepository.findAllByLogInType(LogInType.GUEST);

        int guestUserSize = guestUserEntities.size() + 1;

        String guestEmail = "anonymous" + guestUserSize + "@email.com";

        //빌더 패턴의 장점
        Authority authority = Authority.builder()
                .authorityName("ROLE_ANONYMOUS")
                .build();

        UserEntity user = UserEntity.builder()
                .password(passwordEncoder.encode("NONE"))
                .email(guestEmail)
                .nickName("비회원유저")
                .age(0)
                .gender('M')
                .authorities(Collections.singleton(authority))
                .logInType(LogInType.GUEST)
                .snsId(null)
                .build();

        UserEntity saveUser = userRepository.save(user);

        return new GuestLoginDto(saveUser.getId(), guestEmail, "NONE");
    }

    public TokenDto reissueAccessToken(String refreshToken, String userAgent, String deviceIdentifier) throws BaseException {
        // refresh 토큰이 유효한 경우
        if (tokenProvider.isValidRefreshToken(refreshToken)) {
            RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findRefreshTokenEntityByRefreshTokenAndUserAgentAndDeviceIdentifierAndStatus(refreshToken, userAgent, deviceIdentifier, Status.ACTIVE)
                    .orElseThrow(() -> new BaseException(INVALID_REFRESH_TOKEN));

            // DB에 저장된 refresh 토큰과 동일한 경우 -> access, refresh 토큰 둘다 발급
            // access 재발급 할때마다 refresh 토큰도 재발급하면 보안에 좋음
            if (isEqualToSavedRefreshToken(refreshTokenEntity, refreshToken)) {
                TokenDto tokenDto = tokenProvider.reIssueAccessAndRefreshToken(refreshToken);

                refreshTokenEntity.updateRefreshToken(tokenDto.getRefreshToken());
                refreshTokenRepository.save(refreshTokenEntity);
                return tokenDto;
            }

            // DB에 저장된 refresh 토큰과 일치하지 않는 경우
            else {
                throw new BaseException(INVALID_REFRESH_TOKEN);
            }
        }

        // refresh 토큰 또한 만료된 경우
        else if (tokenProvider.isExpiredRefreshToken(refreshToken)) {
            throw new BaseException(EXPIRED_REFRESH_TOKEN);
        }

        // refresh 토큰이 잘못된 경우
        else {
            throw new BaseException(INVALID_REFRESH_TOKEN);
        }
    }

    public boolean isEqualToSavedRefreshToken(RefreshTokenEntity refreshTokenEntity, String refreshToken) throws BaseException {
        if (refreshTokenEntity.getRefreshToken().equals(refreshToken)) {
            return true;
        }
        return false;
    }

    public void logout(Long userId) {
        UserEntity foundUserEntity = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        List<RefreshTokenEntity> refreshTokenList = refreshTokenRepository.findAllByUserAndStatus(foundUserEntity, Status.ACTIVE);

        refreshTokenRepository.deleteAll(refreshTokenList);
    }

    public UserEntity googleLogin(String accessToken) throws BaseException {
        try{
            String GOOGLE_USERINFO_REQUEST_URL="https://www.googleapis.com/oauth2/v1/userinfo";

            //header에 accessToken을 담는다.
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization","Bearer "+ accessToken);

            //HttpEntity를 하나 생성해 헤더를 담아서 restTemplate으로 구글과 통신하게 된다.
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
            ResponseEntity<String> response = restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);

            System.out.println("response.getBody() = " + response.getBody());

            if (response.getStatusCodeValue() == 200) {
                GoogleUser googleUser = objectMapper.readValue(response.getBody(), GoogleUser.class);

                UserEntity user = userRepository.findUserEntityBySnsId(googleUser.getId())
                        .orElse(googleUser.toEntity(passwordEncoder.encode("NONE")));

                return userRepository.save(user);
            }
            else {
                throw new BaseException(INVALID_ACCESS_GOOGLE);
            }
        }
        catch (Exception exception){
            throw new BaseException(BaseResponseStatus.INVALID_ACCESS_GOOGLE);
        }
    }

    public UserEntity kakaoLogin(String accessToken) throws BaseException {
        try{
            String GOOGLE_USERINFO_REQUEST_URL="https://kapi.kakao.com/v2/user/me";

            //header에 accessToken을 담는다.
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization","Bearer "+ accessToken);

            //HttpEntity를 하나 생성해 헤더를 담아서 restTemplate으로 구글과 통신하게 된다.
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
            ResponseEntity<String> response = restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);

            System.out.println("response.getBody() = " + response.getBody());

            if (response.getStatusCodeValue() == 200) {
                KakaoUser kakaoUser = objectMapper.readValue(response.getBody(), KakaoUser.class);

                UserEntity user = userRepository.findUserEntityBySnsId(kakaoUser.getId())
                        .orElse(kakaoUser.toEntity(passwordEncoder.encode("NONE")));

                return userRepository.save(user);
            }
            else {
                throw new BaseException(INVALID_ACCESS_GOOGLE);
            }
        }
        catch (Exception exception){
            throw new BaseException(BaseResponseStatus.INVALID_ACCESS_GOOGLE);
        }
    }
}
