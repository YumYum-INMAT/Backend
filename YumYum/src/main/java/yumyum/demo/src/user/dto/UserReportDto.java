package yumyum.demo.src.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserReportDto {
    @NotBlank(message = "신고 내용을 입력해주세요")
    @Size(max = 255, message = "최대 255자까지 입력 가능합니다")
    private String contents;
}
