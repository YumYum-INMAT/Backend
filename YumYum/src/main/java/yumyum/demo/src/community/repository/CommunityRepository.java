package yumyum.demo.src.community.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class CommunityRepository {
    private final JdbcTemplate jdbcTemplate;

    public CommunityRepository(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Long createPost(String username, String topic, String contents, String imgUrl) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("post").usingGeneratedKeyColumns("post_id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("topic", topic);
        parameters.put("contents", contents);
        parameters.put("img_url", imgUrl);

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        return key.longValue();
    }

    public Long updatePost(String username, Long post_id, String topic, String contents, String imgUrl) {
        this.jdbcTemplate.update(
                "update post set topic = ?, contents = ?, imgUrl= ? where post_id = ? and username = ? ",
                topic, contents, imgUrl, post_id, username
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

}
