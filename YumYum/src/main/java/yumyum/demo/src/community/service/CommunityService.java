package yumyum.demo.src.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yumyum.demo.config.BaseException;
import yumyum.demo.config.Status;
import yumyum.demo.src.community.dto.*;
import yumyum.demo.src.community.entity.CommentEntity;
import yumyum.demo.src.community.entity.PostEntity;
import yumyum.demo.src.community.entity.PostLikeEntity;
import yumyum.demo.src.community.repository.CommentRepository;
import yumyum.demo.src.community.repository.CommunityRepository;
import yumyum.demo.src.community.repository.PostLikeRepository;
import yumyum.demo.src.community.repository.PostRepository;
import yumyum.demo.src.user.entity.UserEntity;
import yumyum.demo.src.user.repository.UserRepository;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static yumyum.demo.config.BaseResponseStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;

    /*public void createPost(String username, PostDto postDto) throws BaseException {
       try {
           communityRepository.createPost(username, postDto);
       } catch (Exception exception){
           throw new BaseException(DATABASE_ERROR);
       }
    }*/

    public void createPost(Long userId, PostDto postDto) throws BaseException{
        try{
            UserEntity userEntity = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
                    .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

            PostEntity postEntity = new PostEntity(
                    userEntity,
                    postDto.getImgUrl(),
                    postDto.getTopic(),
                    postDto.getContents());

            postRepository.save(postEntity);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


    /*public Long updatePost (String username, Long postId, PostDto postDto) throws BaseException{

        // 수정하는 게시물의 작성자가 내 계정과 같은지 검사하기
        if(username.equals(communityRepository.findUsernameByPostId(postId))){
            try {
                return communityRepository.updatePost(postId, postDto);
            } catch (Exception exception){
                throw new BaseException(DATABASE_ERROR);
            }
        }
        else {
            throw new BaseException(FAILED_TO_UPDATE_POST);
        }
    }*/

    public void updatePost(Long userId, Long postId, PostDto postDto) throws BaseException{
        UserEntity userEntity = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_POST));

        Status status = postEntity.getStatus();
        //삭제된 게시글인지 확인하기
        if(status.equals(Status.INACTIVE)){
            throw new BaseException(DELETED_POST);
        }
        else if(status.equals(Status.ACTIVE)) {
            //게시글 작성자와 내가 동일인물인지 비교하기
            if (postEntity.getUser().equals(userEntity)) {
                try {
                    postEntity.updatePost(postDto.getImgUrl(), postDto.getTopic(), postDto.getContents());
                    postRepository.save(postEntity);

                } catch (Exception exception) {
                    throw new BaseException(DATABASE_ERROR);
                }
            } else {
                throw new BaseException(FAILED_TO_UPDATE_POST);
            }
        }
        else{
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public void deletePost(Long userId, Long postId) {
        UserEntity userEntity = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(DATABASE_ERROR));

        Status status = postEntity.getStatus();

        if (status.equals(Status.ACTIVE)) {
            if (postEntity.getUser().equals(userEntity)) {
                try {
                    postEntity.setStatus(Status.INACTIVE);

                    postRepository.save(postEntity);
                } catch (Exception exception) {
                    throw new BaseException(DATABASE_ERROR);
                }
            } else {
                throw new BaseException(FAILED_TO_DELETE_POST);
            }
        }
        else if(status.equals(Status.INACTIVE)){
            throw new BaseException(DELETED_POST);
        }
        else {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String checkPostStatus(Long post_id){
        return communityRepository.checkPostStatus(post_id);
    }

    @Transactional
    public Long createComment(String username, Long postId, CommentDto commentDto) throws BaseException {
       String status = checkPostStatus(postId);
        if(status.equals("ACTIVE")) {
           try {
               Long commentId = communityRepository.createComment(username, postId, commentDto);
               communityRepository.increaseCountComment(postId);
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

    public void createComment2(String username, Long postId, CommentDto commentDto) throws BaseException{
        UserEntity userEntityByUsername = userRepository.findUserEntityByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        PostEntity postEntityByPostId = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(DATABASE_ERROR));

        CommentEntity commentEntityByUserAndPost = new CommentEntity(
                userEntityByUsername,
                postEntityByPostId,
                commentDto.getContents());

        if(postEntityByPostId.getStatus().equals(Status.ACTIVE)){
            postEntityByPostId.increaseCountComment();

            postRepository.save(postEntityByPostId);
            commentRepository.save(commentEntityByUserAndPost);

        }
        else if(postEntityByPostId.getStatus().equals(Status.INACTIVE)){
            throw new BaseException(UNEXPECTED_ERROR);
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

   /* @Transactional
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
            }
            else if (postLike == 1) {
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
            }
            else {
                throw new BaseException(DATABASE_ERROR);
            }
        }
        else {
            throw new BaseException(DATABASE_ERROR);
        }
    }*/

    public void likePost(Long userId, Long postId) {
        UserEntity userEntity = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_POST));

        //삭제된 게시글인지 확인하기
        if(postEntity.getStatus().equals(Status.ACTIVE)){
            Long count = postLikeRepository.countPostLikeEntityByUserAndPost(userEntity, postEntity);
            //전에 좋아요를 했는지 확인하기
            if(count == 0){
                PostLikeEntity postLikeEntity = new PostLikeEntity(postEntity, userEntity);
                postEntity.increaseCountLike();

                postLikeRepository.save(postLikeEntity);
                postRepository.save(postEntity);
            }
            else if(count == 1){
                PostLikeEntity postLikeEntity = postLikeRepository.findPostLikeEntityByUserAndPost(userEntity, postEntity)
                        .orElseThrow(() -> new BaseException(DATABASE_ERROR));
                Status status = postLikeEntity.getStatus();

                //삭제된 좋아요의 status 확인하기
                if(status.equals(Status.INACTIVE)){
                    postLikeEntity.setStatus(Status.ACTIVE);
                    postEntity.increaseCountLike();

                    postLikeRepository.save(postLikeEntity);
                    postRepository.save(postEntity);
                }
                else if(status.equals(Status.ACTIVE)){
                    throw new BaseException(ALREADY_POST_LIKE);
                }
                else{
                    throw new BaseException(DATABASE_ERROR);
                }
            }
            else {
                throw new BaseException(DATABASE_ERROR);
            }
        }
        else{
            throw new BaseException(DELETED_POST);
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

   /* @Transactional
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

    }*/

    /*public void updateComment(Long comment_id, Long userId, CommentDto commentDto) throws BaseException{
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
    }*/

    public void updateComment(Long commentId, Long userId, CommentDto commentDto) throws BaseException{
        UserEntity userEntity = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        //삭제된 댓글인지 확인
        CommentEntity commentEntity = commentRepository.findCommentEntityByIdAndStatus(commentId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_COMMENT));

        Status status = commentEntity.getPost().getStatus();
        //삭제된 게시글인지 확인
        if(status.equals(Status.ACTIVE)){
            //댓글 작성자와 내가 같은 인물인지 확인
            if(commentEntity.getUser().equals(userEntity)){
                commentEntity.setContents(commentDto.getContents());

                commentRepository.save(commentEntity);
            }
            else{
                throw new BaseException(FAILED_TO_UPDATE_COMMENT);
            }
        }
        else if(status.equals(Status.INACTIVE)){
            throw new BaseException(DELETED_POST);
        }
        else {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public void deleteComment(Long commentId, Long userId) throws BaseException{
        UserEntity userEntity = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        //삭제된 댓글인지 확인
        CommentEntity commentEntity = commentRepository.findCommentEntityByIdAndStatus(commentId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_COMMENT));

        Status status = commentEntity.getPost().getStatus();

        //삭제된 게시글인지 확인
        if(status.equals(Status.ACTIVE)){
            //댓글 작성자와 내가 같은 인물인지 확인
            if(commentEntity.getUser().equals(userEntity)){
                commentEntity.setStatus(Status.INACTIVE);

                PostEntity postEntity = commentEntity.getPost();
                postEntity.decreaseCountComment();

                commentRepository.save(commentEntity);
                postRepository.save(postEntity);
            }
            else{
                throw new BaseException(FAILED_TO_DELETE_COMMENT);
            }
        }
        else if(status.equals(Status.INACTIVE)){
            throw new BaseException(DELETED_POST);
        }
        else {
            throw new BaseException(DATABASE_ERROR);
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


