package yumyum.demo.src.community.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import yumyum.demo.config.BaseEntity;
import yumyum.demo.config.Status;
import yumyum.demo.src.user.entity.UserEntity;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "postLike")
@NoArgsConstructor
@DynamicInsert
public class PostLikeEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postLike_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public PostLikeEntity(PostEntity post, UserEntity user){
        this.post = post;
        this.user = user;
    }

    public void setStatus(Status status){
        this.status = status;
    }
}
