package yumyum.demo.oauth;

import java.util.Collections;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import yumyum.demo.config.LogInType;
import yumyum.demo.src.user.entity.Authority;
import yumyum.demo.src.user.entity.UserEntity;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes; // OAuth2 반환하는 유저 정보 Map
    private String nameAttributeKey;
    private String email;
    private String profile;
    private String snsId;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String email, String profile, String snsId) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.email = email;
        this.profile = profile;
        this.snsId = snsId;
    }

    public static OAuthAttributes of(String oauthType, String userNameAttributeName, Map<String, Object> attributes){
        // KAKAO
        if(oauthType.equals("KAKAO")){
            return ofKakao(userNameAttributeName, attributes);
        }

        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .email((String) attributes.get("email"))
                .profile((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .snsId((String) attributes.get("sub"))
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        // kakao는 kakao_account에 유저정보가 있다. (email)
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
        String snsId = (String) attributes.get("id");

        // kakao_account안에 또 profile이라는 JSON객체가 있다. (nickname, profile_image)
        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .email((String) kakaoAccount.get("email"))
                .profile((String) kakaoProfile.get("profile_image_url"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .snsId(snsId)
                .build();
    }

    public UserEntity toEntity(LogInType logInType){
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        return UserEntity.builder()
                .email(email)
                .password("NONE")
                .authorities(Collections.singleton(authority))
                .logInType(logInType)
                .age(20) //일단 넣어두기, 나중에 빼야함!!
                .gender('F') //일단 넣어두기, 나중에 빼야함!!
                .nickName("구글유저") //일단 넣어두기, 나중에 빼야함!!
                .username(email.substring(0, email.indexOf("@"))) //일단 넣어두기, 나중에 빼야함!!
                .snsId(snsId)
                .build();
    }
}
