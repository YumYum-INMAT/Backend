package yumyum.demo.oauth;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yumyum.demo.config.BaseException;
import yumyum.demo.config.LogInType;
import yumyum.demo.src.user.entity.UserEntity;
import yumyum.demo.src.user.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;


@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws BaseException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String oauthType = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(oauthType, userNameAttributeName, oAuth2User.getAttributes());

        System.out.println("snsId : " + attributes.getSnsId());

        UserEntity userEntity;

        if (oauthType.equals("GOOGLE")) {
            userEntity = saveOrUpdate(attributes, LogInType.GOOGLE);
        }

        else {
            userEntity = saveOrUpdate(attributes, LogInType.KAKAO);
        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(userEntity.getAuthorities().toString())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private UserEntity saveOrUpdate(OAuthAttributes attributes, LogInType logInType) {
        UserEntity user = userRepository.findUserEntityByEmail(attributes.getEmail())
                .orElse(attributes.toEntity(logInType));

        return userRepository.save(user);
    }



}
