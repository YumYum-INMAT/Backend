package yumyum.demo.src.community.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import yumyum.demo.config.BaseException;
import yumyum.demo.config.BaseResponse;
import yumyum.demo.src.community.dto.CommentDto;
import yumyum.demo.src.community.dto.PostDto;
import yumyum.demo.src.community.service.CommunityService;
import yumyum.demo.utils.SecurityUtil;

import java.util.Optional;

import static yumyum.demo.config.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/communities")

public class CommunityController {
    private final CommunityService communityService;

    @PostMapping("")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> createPost(@RequestBody PostDto postDto){
        //유효성 검사하기 - 글자수 검사, null 값 유무 검사
        if(postDto.getTopic().isBlank()){
            return new BaseResponse<>(POST_EMPTY_TOPIC);
        }

        if(postDto.getTopic().length()>45){
            return new BaseResponse<>(POST_OVER_LENGTH_TOPIC);
        }

        if(postDto.getContents().isBlank()){
            return new BaseResponse<>(POST_OVER_LENGTH_CONTENTS);
        }

        if(postDto.getContents().length()>255){
            return new BaseResponse<>(POST_EMPTY_CONTENTS);
        }

        try {
            Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
            communityService.createPost(currentUsername.get(), postDto);
            //글자수 검사, null 값 유무 검사
            String result = "게시물을 작성했습니다";
            return new BaseResponse<>(result);
        }
        catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/{post_id}")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> updatePost(@PathVariable("post_id") Long post_id, @RequestBody PostDto postDto){
        if(postDto.getTopic().isBlank()){
            return new BaseResponse<>(POST_EMPTY_TOPIC);
        }

        if(postDto.getTopic().length()>45){
            return new BaseResponse<>(POST_OVER_LENGTH_TOPIC);
        }

        if(postDto.getContents().isBlank()){
            return new BaseResponse<>(POST_EMPTY_CONTENTS);
        }

        if(postDto.getContents().length()>255){
            return new BaseResponse<>(POST_OVER_LENGTH_CONTENTS);
        }

        try {
            Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
            communityService.updatePost(currentUsername.get(), post_id, postDto);
            String result = "게시물을 수정했습니다";
            return new BaseResponse<>(result);
        }
        catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }

    @PostMapping("/{post_id}/details/comment/{parent_id}")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> createComment(@PathVariable("post_id")Long post_id, @PathVariable(value = "parent_id", required = false) Long parent_id, @RequestBody CommentDto commentDto){
        if(commentDto.getContents().isBlank()){
            return new BaseResponse<>(COMMENT_EMPTY_CONTENTS);
        }
        if(commentDto.getContents().length() > 100){
            return new BaseResponse<>(COMMENT_OVER_LENGTH_CONTENTS);
        }

        if(parent_id == null){
            try{
                Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
                communityService.createComment(currentUsername.get(), post_id, commentDto);
                String result = "댓글을 작성했습니다";
                return new BaseResponse<>(result);
            }
            catch(BaseException e){
                return new BaseResponse<>(e.getStatus());
            }

        }
        else{
            try{
                Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
                communityService.createReplyComment(currentUsername.get(), post_id, parent_id, commentDto);
                String result = "대댓글을 작성했습니다";
                return new BaseResponse<>(result);
            }
            catch (BaseException e){
                return new BaseResponse<>(e.getStatus());
            }
        }

    }
}
