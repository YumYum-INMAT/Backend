package yumyum.demo.src.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PostScreenDto {
    //사용자
    private Long userId;
    //작성자
    private String profileImgUrl;
    private String nickName;

    //게시물 내용
    private Long postId;
    private String topic;
    private String contents;
    private String imgUrl;
    private Long countPostLike;
    private Long countComment;
    private String createdAt;
    // 값이 false면 수정이 안 된 상태고, 값이 true면 수정된 상태이다
    private boolean myLike;

    //댓글
    List<List<CommentInfoDto>> commentInfoDtoList;

    public PostScreenDto(PostInfoDto postinfoDto, List<List<CommentInfoDto>> listList) {
        this.profileImgUrl = postinfoDto.getProfileImgUrl();
        this.nickName = postinfoDto.getNickName();
        this.postId = postinfoDto.getPostId();
        this.topic = postinfoDto.getTopic();
        this.contents = postinfoDto.getContents();
        this.imgUrl = postinfoDto.getImgUrl();
        this.countPostLike = postinfoDto.getCountPostLike();
        this.countComment = postinfoDto.getCountComment();
        this.createdAt = postinfoDto.getCreatedAt();
        this.myLike = postinfoDto.isMyLike();

        this.commentInfoDtoList = listList;
    }

}
