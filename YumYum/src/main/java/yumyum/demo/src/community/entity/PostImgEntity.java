package yumyum.demo.src.community.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import yumyum.demo.config.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "postImg")
@NoArgsConstructor
@DynamicInsert
public class PostImgEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postImg_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    private String imgUrl;

    public PostImgEntity(PostEntity post, String imgUrl){
        this.post = post;
        this.imgUrl = imgUrl;
    }
}
