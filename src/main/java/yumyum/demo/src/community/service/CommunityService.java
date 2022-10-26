package yumyum.demo.src.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yumyum.demo.config.BaseException;
import yumyum.demo.src.community.form.CommentForm;
import yumyum.demo.src.community.form.CommentLikeForm;
import yumyum.demo.src.community.form.PostForm;
import yumyum.demo.src.community.form.PostLikeForm;
import yumyum.demo.src.community.repository.CommunityRepository;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class CommunityService {

    private final CommunityRepository communityRepository;

    @Autowired
    public CommunityService(CommunityRepository communityRepository){
        this.communityRepository = communityRepository;
    }

    //전체 게시물 조회
    public List<PostForm> findAllPost(){
        return communityRepository.findAllPost();
    }

    //특정 게시물 조회
    public Optional<PostForm> findOnePost(Long post_id) throws SQLException {
        try {
            return communityRepository.findOnePost(post_id);
        }
        catch (Exception e){
            throw new SQLException("해당 게시물이 없습니다");
        }
        //post_id가 없을 경우 오류 처리하기
    }

    //게시물 작성
   /* public Long createPost(Long user_id, String topic, String contents){
        //id가 없을 경우, 글자수에 관해 오류 처리하기
        if(topic.length() > 45){
            throw new RuntimeException("제목 글자 수를 초과했습니다");
        }

        if(contents.length()> 200){
            throw new RuntimeException("본문 글자 수를 초과했습니다")
        }
        return communityRepository.createPost(user_id, topic, contents);
    }*/

    //게시글 수정
    public Long updatePost(Long user_id, String topic, String contents){
        return communityRepository.updatePost(user_id,topic,contents);
        //id가 없을 경우, 글자 수에 관한 오류 처리하기
    }

    //게시글 삭제
    public void deletePost(Long post_id){
        communityRepository.deletePost(post_id);
    }

    //게시물에 대한 모든 댓글 조회
    public List<CommentForm> findAllComment(Long post_id){
        return communityRepository.findAllComment(post_id);
        //id가 없을 경우 오류 처리하기
    }

    //특정 댓글 조회
    public Optional<CommentForm> findOneComment(Long post_id, Long comment_id){
        return communityRepository.findOneComment(post_id, comment_id);
        //id가 없을 경우 오류 처리하기
    }

    @Transactional
    //새로운 댓글 작성
    public Long createComment(Long user_id, Long post_id, String contents){
        Long CommentId = communityRepository.createComment(user_id, post_id, contents);
        communityRepository.increseComment(post_id);
        return CommentId;
        //예외 처리 생각해보기
    }

    @Transactional
    //새로운 답글 작성
    public Long createReplyComment(Long user_id, Long post_id, String contents, Long parent_id){
        Long CommentId = communityRepository.createReplyComment(user_id, post_id, contents, parent_id);
        communityRepository.increseComment(post_id);
        return CommentId;
    }


    //댓글 or 답글 수정
    public Long updateComment(Long comment_id, String contents){
        return communityRepository.updateComment(comment_id, contents);
        //id가 없을 경우, 글자 수에 관해 오류처리하기
    }

    @Transactional
    //댓글 삭제
    public void deleteComment(Long post_id, Long comment_id){
        communityRepository.deleteComment(comment_id);
        communityRepository.decreaseComment(post_id);
        //예외 처리 생각해보기
    }

    @Transactional
    //게시물 좋아요 증가
    public Optional<PostLikeForm> increaseLikePost(Long user_id, Long post_id){
        if(communityRepository.countPostLike(post_id,user_id) == 0) {
            Optional<PostLikeForm> postLikeForm = communityRepository.SignUplikePost(user_id, post_id);
            communityRepository.increseLikePost(post_id);
            return postLikeForm;
        }

        else {
            throw new RuntimeException("좋아요를 할 수가 없습니다");
        }
        //예외 처리 및 조건 생각해보기
        //이미 좋아요가 있으면 좋아요 취소 되게 설정


    }

    @Transactional
    //게시물 좋아요 취소
    public void decreseLikePost(Optional<PostLikeForm> postLikeForm){
        communityRepository.deleteLikePost(postLikeForm.get().getId());
        communityRepository.decreseLikePost(postLikeForm.get().getPostId());
        //이미 좋아요가 있을 경우에만 실행하게 한다
    }

    @Transactional
    //댓글 좋아요 증가
    public Optional<CommentLikeForm> increseLikeComment(Long user_id, Long comment_id){
        if(communityRepository.countCommentLike(comment_id, user_id) == 0) {
            Optional<CommentLikeForm> commentLikeForm = communityRepository.SignUplikeComment(user_id, comment_id);
            communityRepository.increseLikeComment(comment_id);
            return commentLikeForm;
        }//예외 처리 및 조건 생각해보기
        //이미 좋아요가 있으면 좋아요 취소 되게 설정
        else {
            throw new RuntimeException("좋아요를 할 수 없습니다");
        }
    }

    @Transactional
    //댓글 좋아요 취소
    public void decreseLikeComment(Optional<CommentLikeForm> commentLikeForm){

        communityRepository.deleteLikeComment(commentLikeForm.get().getId());
        communityRepository.decreseLikeComment(commentLikeForm.get().getCommentId());
        //이미 좋아요가 있을 경우에만 실행하게 한다
    }

    public void createPost(String email, PostForm postForm) throws BaseException {
        communityRepository.createPost(postForm)
    }
}
