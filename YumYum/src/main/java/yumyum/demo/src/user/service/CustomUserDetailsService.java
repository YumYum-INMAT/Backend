package yumyum.demo.src.user.service;



import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import yumyum.demo.config.BaseException;
import yumyum.demo.config.Status;
import yumyum.demo.src.user.entity.UserEntity;
import yumyum.demo.src.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static yumyum.demo.config.BaseResponseStatus.FAILED_TO_LOGIN;
import static yumyum.demo.config.BaseResponseStatus.NOT_ACTIVATED_USER;

@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String id) {
        Optional<UserEntity> foundUser = userRepository.findOneWithAuthoritiesById(Long.parseLong(id));
        if(foundUser.isPresent()) {
            return createUser(id, foundUser.get());
        }
        else {
            throw new UsernameNotFoundException(id + " -> 데이터베이스에서 찾을 수 없습니다.");
        }
    }

    private org.springframework.security.core.userdetails.User createUser(String id, UserEntity userEntity) {
        if (userEntity.getStatus() != Status.ACTIVE) {
            throw new RuntimeException(id + " -> 활성화되어 있지 않습니다.");
        }
        List<GrantedAuthority> grantedAuthorityList = userEntity.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(String.valueOf(userEntity.getId()),
                userEntity.getPassword(),
                grantedAuthorityList);
    }
}
