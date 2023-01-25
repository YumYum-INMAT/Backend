package yumyum.demo.src.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yumyum.demo.config.BaseException;
import yumyum.demo.src.community.dto.*;
import yumyum.demo.src.community.repository.CommunityRepository;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static yumyum.demo.config.BaseResponseStatus.*;

@Service
@Transactional
public class CommunityService {
    private final CommunityRepository communityRepository;

    @Autowired
    public CommunityService(CommunityRepository communityRepository){
        this.communityRepository = communityRepository;
    }

    public void createPost(String username, PostDto postDto) throws BaseException {
       try {
           communityRepository.createPost(username, postDto);
       } catch (Exception exception){
           throw new BaseException(DATABASE_ERROR);
       }
    }
    public Long updatePost (String username, Long post_id, PostDto postDto) throws BaseException{

        // 수정하는 게시물의 작성자가 내 계정과 같은지 검사하기
        if(username.equals(communityRepository.findUsernameByPostId(post_id))){
            try {
                return communityRepository.updatePost(post_id, postDto);
            } catch (Exception exception){
                throw new BaseException(DATABASE_ERROR);
            }
        }
        else {
            throw new BaseException(FAILED_TO_UPDATE_POST);
        }
    }

    public String checkPostStatus(Long post_id){
        return communityRepository.checkPostStatus(post_id);
    }

    @Transactional
    public Long createComment(String username, Long post_id, CommentDto commentDto) throws BaseException {
       String status = checkPostStatus(post_id);
        if(status.equals("ACTIVE")) {
           try {
               Long commentId = communityRepository.createComment(username, post_id, commentDto);
               communityRepository.increaseCountComment(post_id);
               return commentId;
           } catch (Exception exception) {
               throw new BaseException(DATABASE_ERROR);
           }
       } else if (status.equals("INACTIVE")) {
           throw new BaseException(UNEXPECTED_ERROR);
       }
        else {
            throw new BaseException(DATABASE_ERROR);
       }
    }

    @Transactional
    public Long createReplyComment(String username, Long post_id, Long parent_id, CommentDto commentDto) throws BaseException{
        String status = checkPostStatus(post_id);
        if(status.equals("ACTIVE")) {
            try {
                Long commentId = communityRepository.createReplyComment(username, post_id, parent_id, commentDto);
                communityRepository.increaseCountComment(post_id);
                return commentId;
            } catch (Exception exception) {
                throw new BaseException(DATABASE_ERROR);
            }
        } else if (status.equals("INACTIVE")) {
            throw new BaseException(UNEXPECTED_ERROR);
        }
        else {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void likePost(String username, Long post_id) throws BaseException {
        Long user_id = communityRepository.findUserIdByUsername(username);
        Long postLike = communityRepository.countPostLike(post_id, user_id);

        if (communityRepository.checkPostStatus(post_id) == "ACTIVE") {
            if (postLike == 0) {
                try {
                    communityRepository.createPostLike(post_id, user_id);
                    communityRepository.increasePostCountLike(post_id);
                } catch (Exception exception) {
                    throw new BaseException(DATABASE_ERROR);
                }
            } else if (postLike == 1) {
                String status = communityRepository.statusPostLike(post_id, user_id);
                if (status.equals("INACTIVE")) {
                    try {
                        communityRepository.changeStatusPostLike(post_id, user_id, "ACTIVE");
                        communityRepository.increasePostCountLike(post_id);
                    } catch (Exception exception) {
                        throw new BaseException(DATABASE_ERROR);
                    }
                } else if (status.equals("ACTIVE")) {
                    throw new BaseException(ALREADY_POST_LIKE);
                } else {
                    throw new BaseException(DATABASE_ERROR);
                }

            } else {
                throw new BaseException(DATABASE_ERROR);
            }
        }
        else {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void unLikePost(String username, Long post_id) throws BaseException{
        Long user_id = communityRepository.findUserIdByUsername(username);
        Long postLike = communityRepository.countPostLike(post_id, user_id);

        if(communityRepository.checkPostStatus(post_id) == "ACTIVE") {
            if (postLike == 0) {
                throw new BaseException(DATABASE_ERROR);
            } else if (postLike == 1) {
                String status = communityRepository.statusPostLike(post_id, user_id);
                if (status.equals("INACTIVE")) {
                    throw new BaseException(ALREADY_POST_UNLIKE);
                } else if (status.equals("ACTIVE")) {
                    try {
                        communityRepository.changeStatusPostLike(post_id, user_id, "INACTIVE");
                        communityRepository.decreasePostCountLike(post_id);
                    } catch (Exception exception) {
                        throw new BaseException(DATABASE_ERROR);
                    }
                } else {
                    throw new BaseException(DATABASE_ERROR);
                }
            } else {
                throw new BaseException(DATABASE_ERROR);
            }
        }
        else {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void deletePost(String username, Long post_id) throws BaseException{
        // 수정하는 게시물의 작성자가 내 계정과 같은지 검사하기
        if(username.equals(communityRepository.findUsernameByPostId(post_id))){
            try {
                communityRepository.deletePost(post_id);
            } catch (Exception exception){
                throw new BaseException(DATABASE_ERROR);
            }
        }
        else {
            throw new BaseException(FAILED_TO_DELETE_POST);
        }

    }

    public void updateComment(Long comment_id, String username, CommentDto commentDto) throws BaseException{
        //댓글 작성자와 사용자가 일치하는지 유효성 검사
        if(username.equals(communityRepository.findUsernameByCommentId(comment_id))){
            try{
                communityRepository.updateComment(comment_id, commentDto);
            } catch (Exception exception){
                throw new BaseException(DATABASE_ERROR);
            }
        }
        else{
            throw new BaseException(FAILED_TO_UPDATE_COMMENT);
        }
    }

    public void deleteComment( Long comment_id, String username) throws BaseException{
        //댓글 작성자와 사용자가 일치하는지 유효성 검사
        if(username.equals(communityRepository.findUsernameByCommentId(comment_id))){
            try{
                communityRepository.deleteComment(comment_id);
                communityRepository.decreaseCountComment(communityRepository.findPostIdByCommentId(comment_id));
            } catch (Exception exception){
                throw new BaseException(DATABASE_ERROR);
            }
        }
        else{
            throw new BaseException(FAILED_TO_DELETE_COMMENT);
        }

    }

    public void likeComment(String username, Long comment_id) throws BaseException {
        Long user_id = communityRepository.findUserIdByUsername(username);
        Long commentLike = communityRepository.countCommentLike(user_id, comment_id);

            if(commentLike == 0){
                try {
                    communityRepository.createCommentLike(user_id, comment_id);
                    communityRepository.increaseCommentCountLike(comment_id);
                }catch (Exception exception){
                    throw new BaseException(DATABASE_ERROR);
                }
            }
            else if (commentLike == 1) {
                String status = communityRepository.statusCommentLike(user_id, comment_id);
                if(status.equals("INACTIVE")) {
                    try {
                        communityRepository.changeStatusCommentLike(user_id, comment_id, "ACTIVE");
                        communityRepository.increaseCommentCountLike(comment_id);
                    } catch (Exception exception) {
                        throw new BaseException(DATABASE_ERROR);
                    }
                }
                else if (status.equals("ACTIVE")) {
                    throw new BaseException(ALREADY_COMMENT_LIKE);
                }
                else {
                    throw new BaseException(DATABASE_ERROR);
                }
            }
            else {
                throw new BaseException(DATABASE_ERROR);
            }
        }

    public void unLikeComment(String username, Long comment_id) throws BaseException{
        Long user_id = communityRepository.findUserIdByUsername(username);
        Long commentLike = communityRepository.countCommentLike(user_id, comment_id);

        if(commentLike == 0){
            throw new BaseException(COMMENT_LIKE_EMPTY);
        }
        else if(commentLike == 1){
            String status = communityRepository.statusCommentLike(user_id, comment_id);
            if(status.equals("INACTIVE")){
                throw new BaseException(ALREADY_COMMENT_UNLIKE);
            }
            else if (status.equals("ACTIVE")) {
                try{
                communityRepository.changeStatusCommentLike(user_id,comment_id, "INACTIVE");
                communityRepository.decreaseCommentCountLike(comment_id);
            }catch (Exception exception){
                    throw new BaseException(DATABASE_ERROR);
                }
            }
            else {
                throw new BaseException(DATABASE_ERROR);
            }
        }
        else {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public List<CommunityMainDto> getCommunityScreen() throws BaseException{
        try{
            List<CommunityMainDto> communityMainDtoList;
            communityMainDtoList = communityRepository.getCommunityScreen();
            return communityMainDtoList;

        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public PostScreenDto getPostScreen(Long post_id, String username) {
        Long user_id = communityRepository.findUserIdByUsername(username);
        try {
          List<CommentInfoDto> commentInfoDtoList = new ArrayList<>(communityRepository.getCommentInfo(post_id, user_id));
          List<List<CommentInfoDto>> commentInfoDtoMultiList = new ArrayList<>();

          int i = 0;

          List<CommentInfoDto> commentInfoDtoList1 = new ArrayList<>();
          //댓글이 없을때도 고려하자
          if(!commentInfoDtoList.isEmpty()) {
              for (CommentInfoDto commentInfoDto : commentInfoDtoList) {
                  if (commentInfoDto.getGroupNumber() != i) {
                      i = commentInfoDto.getGroupNumber();
                      commentInfoDtoMultiList.add(commentInfoDtoList1);

                      commentInfoDtoList1 = new ArrayList<>();
                      commentInfoDtoList1.add(commentInfoDto);
                  } else if (commentInfoDto.getGroupNumber() == i) {
                      commentInfoDtoList1.add(commentInfoDto);
                  }
              }

              commentInfoDtoMultiList.remove(0);
              commentInfoDtoMultiList.add(commentInfoDtoList1);
          }

          PostScreenDto PostScreenDto = new PostScreenDto(communityRepository.getPostInfo(post_id, user_id), commentInfoDtoMultiList);

            return PostScreenDto;
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }



}


