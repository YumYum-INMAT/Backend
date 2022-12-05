package yumyum.demo.src.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CommunityMainDto {
    //작성자
    private String nickName;

    //게시글
    private Long postId;
    private String topic;
    private String contents;
    private String imgUrl;
    private Long countPostLike;
    private Long countComment;

    //그 외
    private String created_at;

}
