package yumyum.demo.src.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchContentsDto {
    @NotBlank(message = "내용을 입력해주세요.")
    @Size(min = 1, max = 100, message = "내용은 최대 100자까지 입력해주세요.")

    private String contents;
}
