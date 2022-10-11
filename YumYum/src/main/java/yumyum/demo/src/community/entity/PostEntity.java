package yumyum.demo.src.community.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import yumyum.demo.config.BaseEntity;
import yumyum.demo.src.user.entity.UserEntity;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "post")
@NoArgsConstructor
@DynamicInsert
public class PostEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String imgUrl;

    @Column(nullable = false, columnDefinition = "varchar(45)")
    private String topic;

    @Column(nullable = false, columnDefinition = "varchar(200)")
    private String contents;

    @Column(nullable = false, columnDefinition = "default 0")
    private Integer countLike;

    @Column(nullable = false, columnDefinition = "default 0")
    private Integer countComment;

}
