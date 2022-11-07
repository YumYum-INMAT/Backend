package yumyum.demo.src.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yumyum.demo.config.BaseException;
import yumyum.demo.src.community.dto.CommentDto;
import yumyum.demo.src.community.dto.CommentLikeDto;
import yumyum.demo.src.community.dto.PostDto;
import yumyum.demo.src.community.repository.CommunityRepository;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static yumyum.demo.config.BaseResponseStatus.*;

@Service
public class CommunityService {
    private final CommunityRepository communityRepository;

    @Autowired
    public CommunityService(CommunityRepository communityRepository){
        this.communityRepository = communityRepository;
    }

    public Long createPost(String username, PostDto postDto) throws BaseException {
       try {
           return communityRepository.createPost(username, postDto.getTopic(), postDto.getContents(), postDto.getImgUrl());
       } catch (Exception exception){
           throw new BaseException(DATABASE_ERROR);
       }
    }
    public Long updatePost (String username, Long post_id, PostDto postDto) throws BaseException{

        // 수정하는 게시물의 작성자가 내 계정과 같은지 검사하기
        if(username.equals(communityRepository.findUsernameByPostId(post_id))){
            try {
                return communityRepository.updatePost(post_id, postDto.getTopic(), postDto.getContents(), postDto.getImgUrl());
            } catch (Exception exception){
                throw new BaseException(DATABASE_ERROR);
            }
        }
        else {
            throw new BaseException(FAILED_TO_UPDATE_POST);
        }
    }

    @Transactional
    public Long createComment(String username, Long post_id, CommentDto commentDto) throws BaseException {
       try {
           Long commentId = communityRepository.createComment(username, post_id, commentDto.getContents());
           communityRepository.increseCountComment(post_id);
           return commentId;
       } catch (Exception exception){
           throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public Long createReplyComment(String username, Long post_id, Long parent_id, CommentDto commentDto) throws BaseException{
        try {
            Long commentId = communityRepository.createReplyComment(username, post_id, parent_id, commentDto.getContents());
            communityRepository.increseCountComment(post_id);
            return commentId;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }

    @Transactional
    public void likePost(String username, Long post_id) throws BaseException{
        Long user_id = communityRepository.findUserIdByUsername(username);
        if(communityRepository.checkPostLike(post_id, user_id) == 0 ){
            try {
                communityRepository.createPostLike(post_id, user_id);
                communityRepository.incresePostCountLike(post_id);
            }catch (Exception exception){
                throw new BaseException(DATABASE_ERROR);
            }
        }
        else if(communityRepository.checkPostLike(post_id,user_id) == 1){
            throw new BaseException(ALREADY_POST_LIKE);
        }
        else{
            for(int i = 0; i< communityRepository.checkPostLike(post_id, user_id); i++){
                communityRepository.decresePostCountLike(post_id);
            }
            communityRepository.deletePostLike(post_id, user_id);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void unLikePost(String username, Long post_id) throws BaseException{
        Long user_id = communityRepository.findUserIdByUsername(username);
        if(communityRepository.checkPostLike(post_id, user_id) == 1){
            try{
                communityRepository.deletePostLike(post_id, user_id);
                communityRepository.decresePostCountLike(post_id);
            }catch (Exception exception){
                throw new BaseException(DATABASE_ERROR);
            }
        }
        else if(communityRepository.checkPostLike(post_id, user_id) == 0){
            throw new BaseException(ALREADY_POST_UNLIKE);
        }
        else {
            for(int i = 0; i< communityRepository.checkPostLike(post_id, user_id); i++){
                communityRepository.decresePostCountLike(post_id);
            }
            communityRepository.deletePostLike(post_id, user_id);
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
