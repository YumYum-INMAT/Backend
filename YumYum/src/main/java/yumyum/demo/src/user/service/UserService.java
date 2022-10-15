package yumyum.demo.src.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yumyum.demo.config.BaseException;
import yumyum.demo.config.BaseResponse;
import yumyum.demo.jwt.TokenProvider;
import yumyum.demo.src.user.dto.LoginDto;
import yumyum.demo.src.user.dto.UserDto;
import yumyum.demo.src.user.entity.Authority;
import yumyum.demo.src.user.entity.UserEntity;
import yumyum.demo.src.user.repository.UserRepository;
import yumyum.demo.utils.SecurityUtil;

import java.util.Collections;
import java.util.Optional;

import static yumyum.demo.config.BaseResponseStatus.DUPLICATED_EMAIL;
import static yumyum.demo.config.BaseResponseStatus.FAILED_TO_LOGIN;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public UserEntity signup(UserDto userDto) throws BaseException {
        if (userRepository.findOneWithAuthoritiesByEmail(userDto.getEmail()).orElse(null) != null) {
            throw new BaseException(DUPLICATED_EMAIL);
        }

        //빌더 패턴의 장점
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        UserEntity user = UserEntity.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickName(userDto.getNickName())
                .age(10)
                .gender('F')
                .authorities(Collections.singleton(authority))
                .build();

        return userRepository.save(user);
    }


    @Transactional(readOnly = true)
    public Optional<UserEntity> getMyUserWithAuthorities() throws BaseException {
        return SecurityUtil.getCurrentEmail().flatMap(userRepository::findOneWithAuthoritiesByEmail);
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> getUserWithAuthorities(String email) throws BaseException {
        return userRepository.findOneWithAuthoritiesByEmail(email);
    }

    //이메일 존재 여부 체크
    public void checkEmail(String email) throws BaseException {
        if(userRepository.findByEmail(email).isEmpty()) {
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    public void checkPassword(String email, String password) throws BaseException {
        UserEntity user = userRepository.findByEmail(email).get();

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }
}
