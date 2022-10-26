package yumyum.demo.src.community.repository;

import org.springframework.stereotype.Repository;
import yumyum.demo.src.community.entity.CommentEntity;
import yumyum.demo.src.community.entity.CommentLikeEntity;
import yumyum.demo.src.community.entity.PostEntity;
import yumyum.demo.src.community.entity.PostLikeEntity;
import yumyum.demo.src.community.form.CommentForm;
import yumyum.demo.src.community.form.CommentLikeForm;
import yumyum.demo.src.community.form.PostForm;
import yumyum.demo.src.community.form.PostLikeForm;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommunityRepository {

    //모든 게시물 정보 가져오기 (수정: 날짜순으로 가져오기)
    public List<PostForm> findAllPost();

    //모든 게시물 개수 가져오기
    public Long countAllPost();

    //특정 게시물 정보 가져오기
    public Optional<PostForm> findOnePost(Long post_id);

    //게시글 작성
    public Long createPost(Long user_id, String topic, String contents);

    //게시글 수정
    public Long updatePost(Long post_id, String topic, String contents);

    //게시글 삭제
    public void deletePost(Long post_id);

    //(어떤 게시물에서)모든 댓글 정보 가져오기 (수정: 날짜 순으로 가져오기)
    public List<CommentForm> findAllComment(Long post_id);

    //특정 댓글 정보 가져오기
    public Optional<CommentForm> findOneComment(Long post_id, Long comment_id);

    //댓글 작성
    public Long createComment(Long user_id, Long post_id, String contents);

    //답글 작성
    public Long createReplyComment(Long user_id, Long post_id, String contents, Long parent_id);

    //댓글, 답글 수정
    public Long updateComment(Long comment_id, String contents);

    //댓글 삭제
    public void deleteComment(Long comment_id);

    //댓글 수 증가
    public void increseComment(Long post_id);

    //댓글 수 감소
    public void decreaseComment(Long post_id);

    //postLike table에 등록
    public Optional<PostLikeForm> SignUplikePost(Long user_id, Long post_id);

    //postLike table에서 삭제
    public void deleteLikePost(Long post_like_id);

    //post 좋아요 수 증가
    public void increseLikePost(Long post_id);

    //post 좋아요 수 감소
    public void decreseLikePost(Long post_id);

    //postLike table에 row 개수 확인
    public Long countPostLike(Long post_id, Long user_id);

    //commentLike table에 등록
    public Optional<CommentLikeForm> SignUplikeComment(Long user_id, Long comment_id);

    //commentLike table에서 삭제
    public void deleteLikeComment (Long comment_like_id );

    //comment 좋아요 수 증가
    public void increseLikeComment(Long comment_id);

    //comment 좋아요 수 감소
    public void decreseLikeComment(Long comment_id);

    //commentLike table에 row 개수 확인
    public Long countCommentLike(Long comment_id, Long user_id);
}
