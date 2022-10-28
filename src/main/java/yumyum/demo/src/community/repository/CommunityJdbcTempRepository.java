package yumyum.demo.src.community.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import yumyum.demo.src.community.dto.CommentDto;
import yumyum.demo.src.community.dto.CommentLikeDto;
import yumyum.demo.src.community.dto.PostDto;
import yumyum.demo.src.community.dto.PostLikeDto;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.*;

@Repository
public class CommunityJdbcTempRepository{

    private final JdbcTemplate jdbcTemplate;

    public CommunityJdbcTempRepository(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<PostDto> findAllPost() {
        List<PostDto> postFormList = this.jdbcTemplate.query(
                "select  from post order by created_at",
                (rs, rowNum) ->{
                    PostDto postForm = new PostDto();
                    postForm.setId(rs.getLong("post_id"));
                    postForm.setContents(rs.getString("contents"));
                    postForm.setCountComment(rs.getInt("count_comment"));
                    postForm.setCountLike(rs.getInt("count_like"));
                    postForm.setImgUrl(rs.getString("img_url"));
                    postForm.setTopic(rs.getString("topic"));
                    postForm.setUserId(rs.getLong("user_id"));

                    return postForm;
                } );

        return postFormList;
    }

    @Override
    public Long countAllPost() {
        Long count = this.jdbcTemplate.queryForObject(
                "select count(*) from post ", Long.class);

        return count;
    }

    @Override
    public Optional<PostDto> findOnePost(Long post_id) {
        Optional<PostDto> postForm = this.jdbcTemplate.queryForObject(
                "select * from post where post_id = ?",
                (rs, rowNum) -> {
                    PostDto postForm1 = new PostDto();
                    postForm1.setId(rs.getLong("post_id"));
                    postForm1.setContents(rs.getString("contents"));
                    postForm1.setCountComment(rs.getInt("count_comment"));
                    postForm1.setCountLike(rs.getInt("count_like"));
                    postForm1.setImgUrl(rs.getString("img_url"));
                    postForm1.setTopic(rs.getString("topic"));
                    postForm1.setUserId(rs.getLong("user_id"));
                    return Optional.ofNullable(postForm1);
                    }, post_id);

       return postForm;
    }


    public Long createPost(String user_name, String topic, String contents, String imgUrl) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("post").usingGeneratedKeyColumns("comment_id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("user_name", user_name);
        parameters.put("topic", topic);
        parameters.put("contents", contents);
        parameters.put("img_url", imgUrl);

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        return key.longValue();
    }


    public Long updatePost(String user_name, Long post_id, String topic, String contents, String imgUrl) {
        this.jdbcTemplate.update(
                "update post set topic = ?, contents = ?, imgUrl= ? where post_id = ? and user_name = ? ",
                topic, contents, imgUrl, post_id, user_name
        );

        return post_id;
    }

    @Override
    public void deletePost(Long post_id) {
        this.jdbcTemplate.update(
                "delete post where post_id = ?",
                post_id
        );
    }

    @Override
    public List<CommentDto> findAllComment(Long post_id) {
        List<CommentDto> commentFormList = this.jdbcTemplate.query(
                "select * from comment where post_id = ?",
                (rs, rowNum) -> {
                    CommentDto commentDto = new CommentDto();
                    commentDto.setId(rs.getLong("comment_id"));
                    commentDto.setCommentLevel(rs.getInt("comment_level"));
                    commentDto.setCountLike(rs.getInt("count_like"));
                    commentDto.setGroupNumber(rs.getInt("group_number"));
                    commentDto.setParent_id(rs.getLong("parent_id"));
                    commentDto.setPostId(rs.getLong("post_id"));
                    commentDto.setUserId(rs.getLong("user_id"));

                    return commentDto;
                }, post_id);
        return commentFormList;
    }


    @Override
    public Optional<CommentDto> findOneComment(Long post_id, Long comment_id) {

        Optional<CommentDto> commentForm = this.jdbcTemplate.queryForObject(
                "select * from comment where post_id = ? and comment_id = ?",
                (rs, rowNum) ->{
                    CommentDto commentForm1 = new CommentDto();
                    commentForm1.setId(rs.getLong("comment_id"));
                    commentForm1.setCommentLevel(rs.getInt("comment_level"));
                    commentForm1.setCountLike(rs.getInt("count_like"));
                    commentForm1.setGroupNumber(rs.getInt("group_number"));
                    commentForm1.setParent_id(rs.getLong("parent_id"));
                    commentForm1.setPostId(rs.getLong("post_id"));
                    commentForm1.setUserId(rs.getLong("user_id"));

                    return Optional.ofNullable(commentForm1);
                }, post_id, comment_id);

        return commentForm;
    }

    public Long createComment(String user_name, Long post_id, String contents) {

       /* SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("comment").usingGeneratedKeyColumns("comment_id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("user_id", user_id);
        parameters.put("post_id", post_id);

        parameters.put("contents", contents);

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        return key.longValue();*/


        //group number는 최초 댓글일 경우에는 1로, 최초 댓글이 아닐 경우는 group_number의 값들 중 (가장 큰 값 + 1) 로 할당
        String qurry = "insert into comment(group_number,comment_level, parent_id, post_id, user_name , contents)"+
                "values( if((select count(*) from comment) = 0, 1, (select max(group_number) from comment) + 1), 0, 0 , ? , ? ,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = (connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(qurry, new String[]{"comment_id"});
            preparedStatement.setLong(1, post_id);
            preparedStatement.setString(2, user_name);
            preparedStatement.setString(3,contents);

            return preparedStatement;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public Long createReplyComment(Long user_id, Long post_id, String contents, Long parent_id) {
        //parent_id의 group_number을 답글의 group_number에 넣음

        String qurry = "insert into comment(group_number, comment_level, parent_id, post_id, user_id, contents)"+
                "values((select distinct group_number from comment where comment_id = ?), 1 , ? ,? ,? ,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = (connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(qurry, new String[]{"comment_id"});
            preparedStatement.setLong(1, parent_id);
            preparedStatement.setLong(2, parent_id);
            preparedStatement.setLong(3, post_id);
            preparedStatement.setLong(4, user_id);
            preparedStatement.setString(5, contents);

            return preparedStatement;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().longValue();

    }

    @Override
    public Long updateComment(Long comment_id, String contents) {
        this.jdbcTemplate.update(
                "update comment set contents = ? where comment_id = ?",
                contents, comment_id
        );

        return comment_id;
    }

    @Override
    public void deleteComment(Long comment_id) {
        this.jdbcTemplate.update(
                "delete comment where comment_id = ?",
                comment_id
        );
    }

    @Override
    public void increseComment(Long post_id) {
        this.jdbcTemplate.update(
                "update post set count_comment = count_comment + 1 where post_id = ?",
                post_id
        );
    }

    @Override
    public void decreaseComment(Long post_id) {
        this.jdbcTemplate.update(
                "update post set count_comment = count_comment - 1 where post_id = ?",
                post_id
        );
    }

    @Override
    public Optional<PostLikeDto> SignUplikePost(Long user_id, Long post_id) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("post_like").usingGeneratedKeyColumns("post_like_id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("post_id", post_id);
        parameters.put("user_id", user_id);

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        PostLikeDto postLikeDto = new PostLikeDto();
        postLikeDto.setId(key.longValue()); //post_like_id
        postLikeDto.setPostId(post_id);
        postLikeDto.setUserId(user_id);

        return Optional.ofNullable(postLikeDto);
    }

    @Override
    public void deleteLikePost(Long post_like_id) {
        this.jdbcTemplate.update(
                "delete from post_like where post_like_id = ?",
                post_like_id
        );
    }

    @Override
    public void increseLikePost(Long post_id) {
        this.jdbcTemplate.update(
                "update post set countLike = countLike + 1 where post_id = ?",
                post_id
        );
    }

    @Override
    public void decreseLikePost(Long post_id) {
        this.jdbcTemplate.update(
                "update  post set countLike = countLike - 1 where post_id = ?",
                post_id
        );
    }

    @Override
    public Long countPostLike(Long post_id, Long user_id) {
        Long count = this.jdbcTemplate.queryForObject(
                "select count(*) from post_like where post_id = ? and user_id = ?", Long.class, post_id, user_id
        );

        return count;
    }

    @Override
    public Optional<CommentLikeDto> SignUplikeComment(Long user_id, Long comment_id) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("comment_like").usingGeneratedKeyColumns("commentLike_id");
        Map<String, Object> parameters = new HashMap<>();
        //post_id도 인수로 받아야 하지 않나?
        parameters.put("user_id", user_id);
        parameters.put("comment_id", comment_id);

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        CommentLikeDto commentLikeDto = new CommentLikeDto();
        commentLikeDto.setId(key.longValue()); //comment_like_id
        commentLikeDto.setUserId(user_id);
        commentLikeDto.setCommentId(comment_id);

        return Optional.ofNullable(commentLikeDto);
    }

    @Override
    public void deleteLikeComment(Long comment_like_id) {
        this.jdbcTemplate.update(
                "delete comment_like where comment_like_id = ?",
                comment_like_id
        );
    }

    @Override
    public void increseLikeComment(Long comment_id) {
        this.jdbcTemplate.update(
                "update comment set countLike = countLike + 1 where comment_id = ?",
                comment_id
        );
    }

    @Override
    public void decreseLikeComment(Long comment_id) {
        this.jdbcTemplate.update(
                "update comment set countLike = countLike - 1 where comment_id = ?",
                comment_id
        );
    }

    @Override
    public Long countCommentLike(Long comment_id, Long user_id) {
        Long count = this.jdbcTemplate.queryForObject(
                "select count(*) from comment_like where comment_id = ? and user_id = ?", Long.class, comment_id, user_id
        );
        return count;
    }
}
