package yumyum.demo.src.community.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import yumyum.demo.src.restaurant.dto.ImgUrlDto;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PostDto {
    private List<ImgUrlDto> imgUrlDtoList;

    @ApiModelProperty(example = "냠냠을 소개합니다")
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(min = 1, max = 100, message = "제목은 최대 45자까지 입력해주세요.")
    private String topic;

    @ApiModelProperty(example = "프론트엔드,백엔드,디자인")
    @NotBlank(message = "내용을 입력해주세요.")
    @Size(min = 1, max = 3000, message = "내용은 최대 255자까지 입력해주세요.")
    private String contents;

}
