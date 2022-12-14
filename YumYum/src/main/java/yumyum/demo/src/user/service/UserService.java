package yumyum.demo.src.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yumyum.demo.config.BaseException;
import yumyum.demo.config.Status;
import yumyum.demo.jwt.TokenProvider;
//import yumyum.demo.src.user.dto.HeartRestaurantDto;
import yumyum.demo.src.user.dto.MyPageDto;
import yumyum.demo.src.user.dto.SignUpDto;
import yumyum.demo.src.user.dto.UserProfileDto;
import yumyum.demo.src.user.entity.Authority;
import yumyum.demo.src.user.entity.UserEntity;
import yumyum.demo.src.user.repository.UserRepository;
import yumyum.demo.utils.SecurityUtil;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static yumyum.demo.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
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

    public MyPageDto getMyPage(String username) throws BaseException {
        UserEntity foundUserEntity = userRepository.findUserEntityByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        MyPageDto myPageDto = MyPageDto.builder()
                .profileImgUrl(foundUserEntity.getProfileImgUrl())
                .nickName(foundUserEntity.getNickName())
                .build();

        return myPageDto;
    }

    public UserProfileDto getUserProfile(String username) throws BaseException {
        UserEntity foundUserEntity = userRepository.findUserEntityByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        UserProfileDto userProfile = UserProfileDto.builder()
                .profileImgUrl(foundUserEntity.getProfileImgUrl())
                .nickName(foundUserEntity.getNickName())
                .age(foundUserEntity.getAge())
                .gender(foundUserEntity.getGender())
                .build();

        return userProfile;
    }

    public void updateUserProfile(String username, UserProfileDto userProfileDto) throws BaseException {
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


//    public List<HeartRestaurantDto> getHeartRestaurant(String username) throws BaseException {
//        Long userId = userRepository.findUserEntityByUsername(username).get().getId();
//
//
//    }
}
