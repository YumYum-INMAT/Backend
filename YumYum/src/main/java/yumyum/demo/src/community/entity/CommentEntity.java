package yumyum.demo.src.community.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import yumyum.demo.config.BaseEntity;
import yumyum.demo.src.user.entity.UserEntity;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "comment")
@NoArgsConstructor
@DynamicInsert
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(nullable = false, columnDefinition = "varchar(100)")
    private String contents;

    @Column(nullable = false)
    private Long parent_id;

    @Column(nullable = false)
    private Integer commentLevel;

    @Column(nullable = false)
    private Integer groupNumber;

    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer countLike;

}
