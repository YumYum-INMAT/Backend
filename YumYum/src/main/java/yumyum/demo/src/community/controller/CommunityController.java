package yumyum.demo.src.community.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import yumyum.demo.config.BaseException;
import yumyum.demo.config.BaseResponse;
import yumyum.demo.src.community.dto.*;
import yumyum.demo.src.community.service.CommunityService;
import yumyum.demo.utils.SecurityUtil;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static yumyum.demo.config.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/communities")

public class CommunityController {
    private final CommunityService communityService;

    @ApiOperation(value = "게시글 작성 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 4000, message = "데이터베이스 연결에 실패하였습니다."),

            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    //게시글 작성 api
    @PostMapping("")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> createPost(@RequestBody @Valid PostDto postDto){
        //유효성 검사하기 - 글자수 검사, null 값 유무 검사

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


    @ApiOperation(value = "게시글 수정 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 4000, message = "데이터베이스 연결에 실패하였습니다."),

            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    //게시글 수정 api
    @PatchMapping("/{post_id}")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> updatePost(@PathVariable( "post_id") Long post_id, @RequestBody @Valid PostDto postDto){

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

    @ApiOperation(value = "게시글 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 4000, message = "데이터베이스 연결에 실패하였습니다."),

            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    //게시글 삭제 api
    @PatchMapping("/{post_id}/delete")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> deletePost(@PathVariable("post_id")Long post_id){
        try{
            Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
            communityService.deletePost(currentUsername.get(), post_id);
            String result = "게시물을 삭제했습니다";
            return new BaseResponse<>(result);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "댓글,대댓글 작성 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 4000, message = "데이터베이스 연결에 실패하였습니다."),

            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    //댓글,대댓글 작성 api
    @PostMapping(value = {"/{post_id}/details/comment" , "/{post_id}/details/comment/{parent_id}"})
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> createComment(@PathVariable("post_id")Long post_id, @PathVariable(value = "parent_id", required = false) Long parent_id, @RequestBody @Valid CommentDto commentDto){

        if(parent_id == null){
            try{
                Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
                communityService.createComment(currentUsername.get(), post_id, commentDto);
                String result = "댓글을 작성했습니다";
                return new BaseResponse<>(result);
            } catch(BaseException e){
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

    @ApiOperation(value = "댓글 수정 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 4000, message = "데이터베이스 연결에 실패하였습니다."),

            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    //댓글 수정 api
    @PatchMapping("/details/comment/{comment_id}")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> updateComment(@PathVariable("comment_id")Long comment_id, @RequestBody @Valid CommentDto commentDto){

        try{
            Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
            communityService.updateComment(comment_id,currentUsername.get(), commentDto);
            String result = "댓글을 수정했습니다";
            return new BaseResponse<>(result);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
    @ApiOperation(value = "댓글 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 4000, message = "데이터베이스 연결에 실패하였습니다."),

            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    //댓글 삭제 api
    @PatchMapping("/details/comment/{comment_id}/delete")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> deleteComment( @PathVariable("comment_id")Long comment_id){
        try{
            Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
            communityService.deleteComment(comment_id, currentUsername.get());
            String result = "댓글을 삭제했습니다";
            return new BaseResponse<>(result);
        }  catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "게시글 좋아요 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 4000, message = "데이터베이스 연결에 실패하였습니다."),

            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    //게시글 좋아요 설정 api
    @PostMapping("/{post_id}/details/like")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> likePost(@PathVariable("post_id") Long post_id ){
        try{
            Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
            communityService.likePost(currentUsername.get(), post_id);
            String result = "게시글 좋아요를 했어요";
            return new BaseResponse<>(result);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation(value = "게시글 좋아요 취소 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 4000, message = "데이터베이스 연결에 실패하였습니다."),

            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    //게시글 좋아요 취소 api
    @PatchMapping("/{post_id}/details/like/delete")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> unLikePost(@PathVariable("post_id") Long post_id){
        try{
            Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
            communityService.unLikePost(currentUsername.get(), post_id);
            String result = "게시글 좋아요를 취소했어요";
            return new BaseResponse<>(result);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }

    @ApiOperation(value = "댓글 좋아요 설정 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 4000, message = "데이터베이스 연결에 실패하였습니다."),

            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    //댓글 좋아요 설정
    @PostMapping("/details/comment/{comment_id}/like")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> likeComment( @PathVariable("comment_id")Long comment_id){
        try{
            Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
            communityService.likeComment(currentUsername.get(), comment_id);
            String result = "댓글 좋아요를 했어요";
            return new BaseResponse<>(result);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


    @ApiOperation(value = "댓글 좋아요 취소 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 4000, message = "데이터베이스 연결에 실패하였습니다."),

            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    //댓글 좋아요 취소
    @PatchMapping("/details/comment/{comment_id}/like/delete")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> unLikeComment( @PathVariable("comment_id")Long comment_id){
        try{
            Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
            communityService.unLikeComment(currentUsername.get(), comment_id);
            String result = "댓글 좋아요를 취소했어요";
            return new BaseResponse<>(result);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


    @ApiOperation(value = "특정 게시물 조회 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 4000, message = "데이터베이스 연결에 실패하였습니다."),

            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    //특정 게시물 조회
    @GetMapping("/{post_id}")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<PostScreenDto> getPostScreen(@PathVariable("post_id") Long post_id){
        try {
            Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
            return new BaseResponse<>(communityService.getPostScreen(post_id, currentUsername.get()));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }

    @ApiOperation(value = "커뮤니티 조회 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 4000, message = "데이터베이스 연결에 실패하였습니다."),

            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    //커뮤니티 화면 조회
    @GetMapping("")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<List<CommunityMainDto>> getCommunityScreen(){
        try{
            return new BaseResponse<>(communityService.getCommunityScreen());
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }








}