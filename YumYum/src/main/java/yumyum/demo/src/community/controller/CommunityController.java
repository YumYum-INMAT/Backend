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
    @PostMapping("")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> createPost(@RequestBody @Valid PostDto postDto){
        try {

            String currentUserId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));
            Long userId = Long.parseLong(currentUserId);

            communityService.createPost(userId, postDto);

            return new BaseResponse<>("게시물을 작성했습니다");
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
    @PatchMapping("/{post_id}")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> updatePost(@PathVariable( "post_id") Long post_id, @RequestBody @Valid PostDto postDto){

        try {
            String currentUserId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));
            Long userId = Long.parseLong(currentUserId);

            communityService.updatePost(userId, post_id, postDto);

            return new BaseResponse<>("게시물을 수정했습니다");
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
    @PatchMapping("/{post_id}/delete")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> deletePost(@PathVariable("post_id")Long post_id){
        try{
            String currentUserId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));
            Long userId = Long.parseLong(currentUserId);

            communityService.deletePost(userId, post_id);

            return new BaseResponse<>("게시물을 삭제했습니다");
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
    @PostMapping(value = {"/{post_id}/details/comment" , "/{post_id}/details/comment/{parent_id}"})
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<GetCommentIdDto> createComment(@PathVariable("post_id")Long post_id, @PathVariable(value = "parent_id", required = false) Long parent_id, @RequestBody @Valid CommentDto commentDto){

        if(parent_id == null){
            try{
                String currentUsername = SecurityUtil.getCurrentUsername()
                        .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

                return new BaseResponse<>(new GetCommentIdDto(communityService.createComment(currentUsername, post_id, commentDto)));
            } catch(BaseException e){
                return new BaseResponse<>(e.getStatus());
            }

        }
        else{
            try{
                String currentUsername = SecurityUtil.getCurrentUsername()
                        .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

                return new BaseResponse<>(new GetCommentIdDto(communityService.createReplyComment(currentUsername, post_id, parent_id, commentDto)));
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
    @PatchMapping("/details/comment/{comment_id}")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> updateComment(@PathVariable("comment_id")Long comment_id, @RequestBody @Valid CommentDto commentDto){

        try{
            String currentUserId = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));
            Long userId = Long.parseLong(currentUserId);

            communityService.updateComment(comment_id, userId, commentDto);

            return new BaseResponse<>("댓글을 수정했습니다");
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
    @PatchMapping("/details/comment/{comment_id}/delete")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> deleteComment( @PathVariable("comment_id")Long comment_id){
        try{
            String currentUsername = SecurityUtil.getCurrentUsername()
                    .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

            communityService.deleteComment(comment_id, currentUsername);

            return new BaseResponse<>("댓글을 삭제했습니다");
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
    @PostMapping("/{post_id}/details/like")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> likePost(@PathVariable("post_id") Long post_id ){
        try{
            String currentUsername = SecurityUtil.getCurrentUsername()
                    .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

            communityService.likePost(currentUsername, post_id);

            return new BaseResponse<>("게시글 좋아요를 했어요");
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
    @PatchMapping("/{post_id}/details/like/delete")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> unLikePost(@PathVariable("post_id") Long post_id){
        try{
            String currentUsername = SecurityUtil.getCurrentUsername()
                    .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

            communityService.unLikePost(currentUsername, post_id);

            return new BaseResponse<>("게시글 좋아요를 취소했어요");
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
    @PostMapping("/details/comment/{comment_id}/like")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> likeComment( @PathVariable("comment_id")Long comment_id){
        try{
            String currentUsername = SecurityUtil.getCurrentUsername()
                    .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

            communityService.likeComment(currentUsername, comment_id);

            return new BaseResponse<>("댓글 좋아요를 했어요");
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
    @PatchMapping("/details/comment/{comment_id}/like/delete")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<String> unLikeComment( @PathVariable("comment_id")Long comment_id){
        try{
            String currentUsername = SecurityUtil.getCurrentUsername()
                    .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

            communityService.unLikeComment(currentUsername, comment_id);

            return new BaseResponse<>("댓글 좋아요를 취소했어요");
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

    @ApiOperation(value = "특정 게시물 조회 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 4000, message = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "잘못된 JWT 토큰입니다."),
            @ApiResponse(code = 403, message = "접근에 권한이 없습니다.")
    })
    @GetMapping("/{post_id}")
    @PreAuthorize("hasAnyRole('USER')")
    public BaseResponse<PostScreenDto> getPostScreen(@PathVariable("post_id") Long post_id){
        try{
            String currentUsername = SecurityUtil.getCurrentUsername()
                    .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

            return new BaseResponse<>(communityService.getPostScreen(post_id, currentUsername));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }





}