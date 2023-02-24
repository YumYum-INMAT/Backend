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

    @Column(nullable = false, columnDefinition = "bigint default 0")
    private Long parent_id;

    @Column(nullable = false)
    private Integer commentLevel;

    @Column(nullable = false)
    private Integer groupNumber;

    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer countLike;

    @Column(nullable = false, columnDefinition = "varchar(100)")
    private String contents;

    //좋아요 수 증가
    public void increaseCountLike(){this.countLike++;}
    //좋아요 수 감소
    public void decreaseCountLike(){this.countLike--;}

    public void setContents(String contents){
        this.contents = contents;
    }
    public void setStatus(Status status){
        this.status = status;
    }
    public void setGroupNumber(Integer groupNumber){
        this.groupNumber = groupNumber;
    }

    public CommentEntity(UserEntity userEntity, PostEntity postEntity, String contents, Integer groupNumber){
        this.user = userEntity;
        this.post = postEntity;
        this.contents = contents;
        this.groupNumber = groupNumber;
        this.commentLevel = 0;
    }

    public CommentEntity(UserEntity user, PostEntity post, String contents, Long parentId, Integer commentLevel){
        this.user = user;
        this.post = post;
        this.parent_id = parentId;
        this.commentLevel = commentLevel;
        this.contents = contents;
    }
}
