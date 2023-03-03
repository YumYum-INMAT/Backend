package yumyum.demo.src.community.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostReportDto {
    @NotBlank(message = "신고 내용을 입력해주세요")
    @Size(max = 255, message = "최대 255자까지 입력 가능합니다")
    private String contents;
}
