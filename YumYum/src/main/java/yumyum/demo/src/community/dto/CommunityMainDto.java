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

public class CommunityMainDto {
    //작성자
    private String nickName;

    //게시글
    private Long postId;
    private String topic;
    private String contents;
    private List<ImgUrlDto> imgUrlDtoList;
    private Integer countPostLike;
    private Integer countComment;

    //그 외
    private String createdAt;

}
