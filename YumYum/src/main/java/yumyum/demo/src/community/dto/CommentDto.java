package yumyum.demo.src.community.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CommentDto {
    @ApiModelProperty(example = "게시물이 좋네요")
    @NotBlank(message = "내용을 입력해주세요.")
    @Size(min = 1, max = 100, message = "내용은 최대 100자까지 입력해주세요.")
    String contents;
}
