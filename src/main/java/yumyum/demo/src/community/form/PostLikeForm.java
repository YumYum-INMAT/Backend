package yumyum.demo.src.community.form;

import yumyum.demo.src.community.entity.PostEntity;
import yumyum.demo.src.user.entity.UserEntity;

import javax.persistence.*;

public class PostLikeForm extends BaseFrom{

    private Long id;

    private PostEntity post;

    private UserEntity user;

    private Long postId;

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
