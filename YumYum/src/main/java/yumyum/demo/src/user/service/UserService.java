package yumyum.demo.src.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yumyum.demo.config.BaseException;
import yumyum.demo.config.BaseResponseStatus;
import yumyum.demo.config.Status;
import yumyum.demo.jwt.TokenProvider;
//import yumyum.demo.src.user.dto.HeartRestaurantDto;
import yumyum.demo.src.community.dto.CommunityMainDto;
import yumyum.demo.src.user.dto.*;
import yumyum.demo.src.user.entity.Authority;
import yumyum.demo.src.user.entity.RefreshTokenEntity;
import yumyum.demo.src.user.entity.UserEntity;
import yumyum.demo.src.user.repository.RefreshTokenRepository;
import yumyum.demo.src.user.repository.UserJdbcTempRepository;
import yumyum.demo.src.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static yumyum.demo.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserJdbcTempRepository userJdbcTempRepository;
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

    public LoginDto anonymousLogin() throws BaseException {
        List<UserEntity> anonymousUserEntities = userRepository.findAllByEmail("anonymous@email.com");

        Integer anonymousUserSize = anonymousUserEntities.size() + 1;

        //빌더 패턴의 장점
        Authority authority = Authority.builder()
                .authorityName("ROLE_ANONYMOUS")
                .build();

        UserEntity user = UserEntity.builder()
                .username("anonymous" + Integer.toString(anonymousUserSize))
                .password(passwordEncoder.encode("1234abcd!"))
                .email("anonymous@email.com")
                .phoneNumber("010-0000-0000")
                .nickName("비회원유저")
                .age(0)
                .gender('M')
                .authorities(Collections.singleton(authority))
                .build();

        userRepository.save(user);

        return new LoginDto("anonymous" + Integer.toString(anonymousUserSize), "1234abcd!");
    }

    public UpdateUserProfileDto getUserProfile(String username) throws BaseException {
        UserEntity foundUserEntity = userRepository.findUserEntityByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        UpdateUserProfileDto userProfile = UpdateUserProfileDto.builder()
                .profileImgUrl(foundUserEntity.getProfileImgUrl())
                .nickName(foundUserEntity.getNickName())
                .age(foundUserEntity.getAge())
                .gender(foundUserEntity.getGender())
                .build();

        return userProfile;
    }

    public void updateUserProfile(String username, UpdateUserProfileDto userProfileDto) throws BaseException {
        UserEntity foundUserEntity = userRepository.findUserEntityByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        //이 전 닉네임과 다른 닉네임으로 바꿀때는 중복 검사 진행
        if (!foundUserEntity.getNickName().equals(userProfileDto.getNickName())) {
            checkNickName(userProfileDto.getNickName());
        }
        //이 전 닉네임으로 그대로 바꿀때는 중복 검사하지 않음
        foundUserEntity.updateUserProfile(
                userProfileDto.getProfileImgUrl(),
                userProfileDto.getNickName(),
                userProfileDto.getAge(),
                userProfileDto.getGender());
        userRepository.save(foundUserEntity);
    }



    public List<CommunityMainDto> getPost(String username) throws BaseException {
        Long user_id = userJdbcTempRepository.findUserIdByUsername(username);

        try{
            List<CommunityMainDto> communityMainDtoList = userJdbcTempRepository.getPost(user_id);

            return communityMainDtoList;
        }catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public List<MyReviewDto> getMyReview(String username) throws BaseException{
        Long user_id = userJdbcTempRepository.findUserIdByUsername(username);

        try{
            List<MyReviewDto> myReviewDtoList = userJdbcTempRepository.getMyReview(user_id);

            return myReviewDtoList;
        }catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public List<MyHeartRestaurantDto> getMyHeartRestaurant(String username) throws BaseException{
        Long user_id = userJdbcTempRepository.findUserIdByUsername(username);

        try{
            List<MyHeartRestaurantDto> myHeartRestaurantDtoList = userJdbcTempRepository.getMyHeartRestaurant(user_id);

            return myHeartRestaurantDtoList;
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public void updateRefreshToken(String username, String refreshToken, String userAgent) throws BaseException {
        UserEntity foundUserEntity = userRepository.findUserEntityByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenRepository.findRefreshTokenEntityByUserAndStatus(foundUserEntity, Status.ACTIVE);

        //첫 로그인인 경우 -> 디비에 저장된 refresh 토큰 칼럼이 없음
        if (refreshTokenEntity.isEmpty()) {
            RefreshTokenEntity createdRefreshTokenEntity = new RefreshTokenEntity(foundUserEntity, refreshToken, userAgent);
            refreshTokenRepository.save(createdRefreshTokenEntity);
        }
        else {
            refreshTokenEntity.get().updateRefreshToken(refreshToken);
            refreshTokenRepository.save(refreshTokenEntity.get());
        }
    }
}
