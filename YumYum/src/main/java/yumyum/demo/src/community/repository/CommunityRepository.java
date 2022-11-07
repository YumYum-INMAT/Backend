package yumyum.demo.src.community.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

@Repository
public class CommunityRepository {
    private final JdbcTemplate jdbcTemplate;

    public CommunityRepository(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Long createPost(String username, String topic, String contents, String imgUrl) {
       /* SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("post").usingGeneratedKeyColumns("post_id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("user_id", findUserIdByUsername(username));
        parameters.put("topic", topic);
        parameters.put("contents", contents);
        parameters.put("img_url", imgUrl);

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        return key.longValue();*/

        String qurry = "insert into post(user_id, topic, contents, img_url)" +
                "values(?, ? ,? ,? )";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = (connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(qurry, new String[]{"post_id"});
            preparedStatement.setLong(1, findUserIdByUsername(username));
            preparedStatement.setString(2, topic);
            preparedStatement.setString(3, contents);
            preparedStatement.setString(4, imgUrl);

            return preparedStatement;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().longValue();

    }

    public Long updatePost(Long post_id, String topic, String contents, String imgUrl) {
        this.jdbcTemplate.update(
                "update post set topic = ?, contents = ?, img_url= ? where post_id = ? ",
                topic, contents, imgUrl, post_id
        );

        return post_id;
    }

    public String findUsernameByPostId(Long post_id){
        String username = this.jdbcTemplate.queryForObject(
                "select user.username from post inner join user on post.user_id = user.user_id where post_id = ?",
                String.class, post_id
        );
        return username;
    }

    public Long findUserIdByUsername(String username){
        Long userId = this.jdbcTemplate.queryForObject(
                "select user_id from user where username = ?",
                Long.class, username
        );
        return userId;
    }

    public Long createComment(String username, Long post_id, String contents) {

        //group number는 최초 댓글일 경우에는 1로, 최초 댓글이 아닐 경우는 group_number의 값들 중 (가장 큰 값 + 1) 로 할당
        String qurry = "insert into comment(group_number,comment_level, parent_id, post_id, user_id , contents)" +
                "values( if((select counting from(select count(*) as counting from comment where post_id = ?) c) = 0, 1, ((select num + 1  from (select max(group_number) as num from comment) as A) ) ),0, 0 , ? , ? , ? )";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = (connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(qurry, new String[]{"comment_id"});
            preparedStatement.setLong(1,post_id);
            preparedStatement.setLong(2, post_id);
            preparedStatement.setLong(3, findUserIdByUsername(username));
            preparedStatement.setString(4, contents);

            return preparedStatement;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public void increseCountComment(Long post_id) {
        this.jdbcTemplate.update(
                "update post set count_comment = count_comment + 1 where post_id = ?",
                post_id
        );
    }

    public void decreseCountComment(Long post_id){
        this.jdbcTemplate.update(
                "update post set count_comment = count_comment - 1  where post_id = ?;",
                post_id
        );
    }

    public Long createReplyComment(String username, Long post_id, Long parent_id, String contents) {
        String qurry = "insert into comment(group_number, comment_level, parent_id, post_id, user_id, contents)" +
                "values((select groupNum from (select distinct group_number as groupNum from comment where comment_id = ?) as c), 1 , ? , ? , ? ,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = (connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(qurry, new String[]{"comment_id"});
            preparedStatement.setLong(1, parent_id);
            preparedStatement.setLong(2, parent_id);
            preparedStatement.setLong(3, post_id);
            preparedStatement.setLong(4, findUserIdByUsername(username));
            preparedStatement.setString(5, contents);

            return preparedStatement;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().longValue();

    }

    //post_like table에 값 추가
    public void createPostLike(Long post_id, Long user_id){
        this.jdbcTemplate.update(
          "insert into post_like (post_id, user_id) values (?, ?);",
                post_id, user_id
        );
    }

    //post_lke table에 값 삭제
    public void deletePostLike(Long post_id, Long user_id){
        this.jdbcTemplate.update(
                "update post_like set status = 0  where post_id = ?, user_id = ?;",
                post_id, user_id
        );
    }

    //post table에 count_like 값 증가
    public void incresePostCountLike(Long post_id){
        this.jdbcTemplate.update(
                "update post set count_like = count_like + 1 where post_id = ?",
                post_id
        );
    }

    //post table count_like 값 감소
    public void decresePostCountLike(Long post_id){
        this.jdbcTemplate.update(
                "update post set count_like = count_like - 1 where post_id = ?",
                post_id
        );
    }


    //post_like table에 이미 값이 있는지 검사
    public Long countPostLike(Long post_id, Long user_id){
        Long count = this.jdbcTemplate.queryForObject("select count(*) from post_like where post_id = ? and user_id = ?"
                , Long.class, post_id, user_id);
        return count;
    }

    //post_like table에 status 값 검사
    public int statusPostLike(Long post_id, Long user_id){
        int status  = this.jdbcTemplate.queryForObject("select status from post_like where post_id = ? and user_id = ?"
        , Integer.class, post_id, user_id);
        return status;
    }

    //게시글 삭제
    public void deletePost(Long post_id) {
        this.jdbcTemplate.update(
                "update post set status = 0 where post_id = ?",
                post_id
        );
    }

    public String findUsernameByCommentId(Long comment_id) {
        String username = this.jdbcTemplate.queryForObject(
                "select user.username from comment inner join user on comment.user_id = user.user_id where comment_id = ?",
                String.class, comment_id
        );
        return username;
    }

    public void updateComment(Long comment_id, String contents) {
        this.jdbcTemplate.update(
                "update comment set contents = ? where comment_id = ? ;",
                contents, comment_id
        );
    }

    public void deleteComment(Long comment_id) {
        this.jdbcTemplate.update(
                "update comment set status = 0  where comment_id = ? ;",
                comment_id
        );
    }

    public Long findPostIdByCommentId(Long comment_id) {
        Long post_id = this.jdbcTemplate.queryForObject(
                "select post_id from comment where comment_id = ?",
                Long.class,comment_id
        );
        return post_id;
    }

    public void changeStatusPostLike(Long post_id, Long user_id, int i) {
        this.jdbcTemplate.update(
                "update post_like set status = ? where post_id = ? and user_id = ?;",
                i, post_id, user_id
        );
    }

    public Long countCommentLike(Long user_id, Long comment_id) {
        Long count = this.jdbcTemplate.queryForObject(
                "select count(*) from comment_like where user_id = ? and comment_id = ?",
                Long.class, user_id, comment_id
        );
        return count;
    }

    public void createCommentLike(Long user_id, Long comment_id) {
        this.jdbcTemplate.update(
                "insert into comment_like (user_id, comment_id) values (?, ?);",
                user_id, comment_id
        );
    }

    public void increseCommentCountLike(Long comment_id) {
        this.jdbcTemplate.update(
                "update comment set count_like = count_like + 1 where comment_id = ?;",
                comment_id
        );
    }

    public int statusCommentLike(Long user_id, Long comment_id) {
        int status = this.jdbcTemplate.queryForObject(
                "select status from comment_like where user_id = ? and comment_id = ?",
                Integer.class, user_id, comment_id
        );
        return status;
    }

    public void changeStatusCommentLike(Long user_id, Long comment_id, int i) {
        this.jdbcTemplate.update(
                "update comment_like set status = ? where user_id = ? and comment_id = ?;",
                i, user_id, comment_id
        );
    }

    public void decreseCommentCountLike(Long comment_id) {
        this.jdbcTemplate.update(
                "update comment set count_like = count_like - 1 where comment_id = ?;",
                comment_id
        );
    }
}
