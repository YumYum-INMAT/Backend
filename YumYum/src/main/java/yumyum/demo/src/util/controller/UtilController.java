package yumyum.demo.src.util.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.File;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yumyum.demo.config.BaseException;
import yumyum.demo.config.BaseResponse;
import yumyum.demo.src.util.dto.FileNameDto;
import yumyum.demo.src.util.service.UtilService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/utils")
public class UtilController {
    private final UtilService utilService;

    @ApiOperation(value = "미리 서명된 URL 획득 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    @PostMapping("/images")
    public BaseResponse<String> getPreSignedUrl(@Valid @RequestBody FileNameDto fileNameDto) {
        try {
            File file = new File(fileNameDto.getFileName());
            String fileName = file.getName();

            return new BaseResponse<>(utilService.getPreSignedUrl(fileName));

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }



}
