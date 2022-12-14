package yumyum.demo.src.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.crypto.password.PasswordEncoder;
import yumyum.demo.config.BaseEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor
@DynamicInsert
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, updatable = false, length = 60)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false, columnDefinition = "char(1)")
    private Character gender;

    @Column(nullable = true) //default
    private String profileImgUrl;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")}    )
    private Set<Authority> authorities;


    @Builder
    public UserEntity(Long id, String username, String password, String email, String phoneNumber, String nickName,
                      Integer age, Character gender, String profileImgUrl, Set<Authority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.nickName = nickName;
        this.age = age;
        this.gender = gender;
        this.profileImgUrl = profileImgUrl;
        this.authorities = authorities;
    }

    // ???????????? ?????????
    public UserEntity encode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
        return this;
    }

    public boolean checkPassword(String plainPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(plainPassword, this.password);
    }

    /**
     * ???????????? ??????
     */
    
    // ????????? ??????
    public void updateUserProfile(String profileImgUrl, String nickName, Integer age, Character gender) {
        this.profileImgUrl = profileImgUrl;
        this.nickName = nickName;
        this.age = age;
        this.gender = gender;
    }

}
