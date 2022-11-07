package yumyum.demo.src.community.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PostDto {
    @ApiModelProperty(example = "img_url")
    @Size(min = 1, message = "null 값 또는 URL을 입력해주세요")
    @URL(message = "올바른 URL이 아닙니다.")
    private String imgUrl;

    @ApiModelProperty(example = "topic")
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(min = 1, max = 45, message = "제목은 최대 45자까지 입력해주세요.")
    private String topic;

    @ApiModelProperty(example = "contents")
    @NotBlank(message = "내용을 입력해주세요.")
    @Size(min = 1, max = 255, message = "내용은 최대 255자까지 입력해주세요.")
    private String contents;

}
