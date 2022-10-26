package yumyum.demo.src.community.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yumyum.demo.config.BaseException;
import yumyum.demo.config.BaseResponse;
import yumyum.demo.src.community.form.PostForm;
import yumyum.demo.src.community.service.CommunityService;
import yumyum.demo.utils.SecurityUtil;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/communities")
public class CommunityController {
    private final CommunityService communityService;

    @PostMapping("")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> createPost(@RequestBody PostForm postForm){
        //유효성 검사하기 - 글자수 검사, null 값 유무 검사

        try {
            Optional<String> currentEmail = SecurityUtil.getCurrentEmail();
            communityService.createPost(currentEmail.get(), postForm);
            //글자수 검사, null 값 유무 검사
        }
        catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


}
