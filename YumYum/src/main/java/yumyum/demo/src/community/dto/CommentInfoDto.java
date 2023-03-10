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

public class CommentInfoDto {

    private Long commentId;
    private String nickName;
    private String profileImgUrl;
    private String contents;
    private Integer countCommentLike;
    private String createdAt;
    private int groupNumber;
    private Long parentId;
    // 값이 false면 수정이 안 된 상태고, 값이 true면 수정된 상태이다
    private boolean checkUpdated;
    // 값이 false면 좋아요를 안 한 상태이고, 값이 true면 좋아요를 한 상태이다
    private boolean myLike;
}
