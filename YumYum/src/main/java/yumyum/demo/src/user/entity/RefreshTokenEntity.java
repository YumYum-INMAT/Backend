package yumyum.demo.src.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import yumyum.demo.config.BaseEntity;

@Entity
@Getter
@Table(name = "refreshToken")
@NoArgsConstructor
@DynamicInsert
public class RefreshTokenEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private String userAgent;

    @Column(nullable = false)
    private String deviceIdentifier;

    public RefreshTokenEntity(UserEntity user, String refreshToken, String userAgent, String deviceIdentifier) {
        this.user = user;
        this.refreshToken = refreshToken;
        this.userAgent = userAgent;
        this.deviceIdentifier = deviceIdentifier;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void updateDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }
}
