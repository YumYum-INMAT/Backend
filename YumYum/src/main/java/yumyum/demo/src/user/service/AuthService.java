package yumyum.demo.src.user.service;

import static yumyum.demo.config.BaseResponseStatus.DUPLICATED_EMAIL;
import static yumyum.demo.config.BaseResponseStatus.DUPLICATED_NICKNAME;
import static yumyum.demo.config.BaseResponseStatus.DUPLICATED_PHONE_NUMBER;
import static yumyum.demo.config.BaseResponseStatus.DUPLICATED_USERNAME;
import static yumyum.demo.config.BaseResponseStatus.EXPIRED_REFRESH_TOKEN;
import static yumyum.demo.config.BaseResponseStatus.FAILED_TO_LOGIN;
import static yumyum.demo.config.BaseResponseStatus.INVALID_REFRESH_TOKEN;
import static yumyum.demo.config.BaseResponseStatus.NOT_ACTIVATED_USER;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yumyum.demo.config.BaseException;
import yumyum.demo.config.Status;
import yumyum.demo.jwt.TokenProvider;
import yumyum.demo.src.user.dto.LoginDto;
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

    @Transactional
    public void signup(SignUpDto signUpDto) throws BaseException {
        //아이디 중복 체크
        if (userRepository.findUserEntityByUsername(signUpDto.getUsername()).isPresent()) {
            throw new BaseException(DUPLICATED_USERNAME);
        }

        //이메일 중복 체크
        if (userRepository.findUserEntityByEmail(signUpDto.getEmail()).isPresent()) {
            throw new BaseException(DUPLICATED_EMAIL);
        }

        //닉네임 중복 체크
        if(userRepository.findUserEntityByNickNameAndStatus(signUpDto.getNickName(), Status.ACTIVE).isPresent()) {
            throw new BaseException(DUPLICATED_NICKNAME);
        }

        //휴대폰 번호 중복 체크
        if(userRepository.findUserEntityByPhoneNumberAndStatus(signUpDto.getPhoneNumber(), Status.ACTIVE).isPresent()) {
            throw new BaseException(DUPLICATED_PHONE_NUMBER);
        }

        //빌더 패턴의 장점
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        UserEntity user = UserEntity.builder()
                .username(signUpDto.getUsername())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .email(signUpDto.getEmail())
                .phoneNumber(signUpDto.getPhoneNumber())
                .nickName(signUpDto.getNickName())
                .age(signUpDto.getAge())
                .gender(signUpDto.getGender())
                .authorities(Collections.singleton(authority))
                .build();

        userRepository.save(user);
    }

    //아이디 존재 여부 체크 -> 로그인에서 사용
    public void checkUsername(String username) throws BaseException {
        if(userRepository.findUserEntityByUsername(username).isEmpty()) {
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    //아이디 중복 여부 체크
    public void checkUsernameDuplicate(String username) throws BaseException {
        if(userRepository.findUserEntityByUsername(username).isPresent()) {
            throw new BaseException(DUPLICATED_USERNAME);
        }
    }

    public void checkPassword(String username, String password) throws BaseException {
        UserEntity user = userRepository.findUserEntityByUsernameAndStatus(username, Status.ACTIVE)
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

    public void updateRefreshToken(String username, String refreshToken, String userAgent, String deviceIdentifier) throws BaseException {
        UserEntity foundUserEntity = userRepository.findUserEntityByUsernameAndStatus(username, Status.ACTIVE)
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

    public LoginDto anonymousLogin() throws BaseException {
        List<UserEntity> anonymousUserEntities = userRepository.findAllByEmail("anonymous@email.com");

        int anonymousUserSize = anonymousUserEntities.size() + 1;

        //빌더 패턴의 장점
        Authority authority = Authority.builder()
                .authorityName("ROLE_ANONYMOUS")
                .build();

        UserEntity user = UserEntity.builder()
                .username("anonymous" + anonymousUserSize)
                .password(passwordEncoder.encode("1234abcd!"))
                .email("anonymous@email.com")
                .phoneNumber("010-0000-0000")
                .nickName("비회원유저")
                .age(0)
                .gender('M')
                .authorities(Collections.singleton(authority))
                .build();

        userRepository.save(user);

        return new LoginDto("anonymous" + anonymousUserSize, "1234abcd!");
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

    public void logout(String username) {
        UserEntity foundUserEntity = userRepository.findUserEntityByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        List<RefreshTokenEntity> refreshTokenList = refreshTokenRepository.findAllByUserAndStatus(foundUserEntity, Status.ACTIVE);

        refreshTokenRepository.deleteAll(refreshTokenList);
    }
}
