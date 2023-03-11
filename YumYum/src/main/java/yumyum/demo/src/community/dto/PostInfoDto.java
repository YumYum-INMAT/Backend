package yumyum.demo.src.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import yumyum.demo.src.restaurant.dto.ImgUrlDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostInfoDto {
    //작성자
    private String nickName;
    private String profileImgUrl;

    //게시물 내용
    private Long postId;
    private Long userId;
    private String topic;
    private String contents;
    private List<ImgUrlDto> imgUrlDtoList;
    private Integer countPostLike;
    private Integer countComment;
    private String createdAt;
    // 값이 false면 좋아요를 안 한 상태이고, 값이 true면 좋아요를 한 상태이다
    private boolean myLike;


}
