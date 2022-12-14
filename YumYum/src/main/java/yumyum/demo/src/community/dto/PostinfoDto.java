package yumyum.demo.src.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostinfoDto {
    //작성자
    private String nickName;
    private String profileImgUrl;
    //게시물 내용

    private Long postId;
    private String topic;
    private String contents;
    private String imgUrl;
    private Long countPostLike;
    private Long countComment;
    private String crated_at;
    // 값이 false면 좋아요를 안 한 상태이고, 값이 true면 좋아요를 한 상태이다
    private boolean myLike;


}
