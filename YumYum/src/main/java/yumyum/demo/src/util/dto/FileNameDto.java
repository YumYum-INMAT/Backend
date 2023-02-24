package yumyum.demo.src.util.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileNameDto {
    @NotBlank(message = "파일 이름을 입력해주세요")
    @Pattern(regexp = "^\\S+.(?i)(jpg|jpeg|png)$", message = "잘못된 파일 형식입니다.")
    private String fileName;
}
