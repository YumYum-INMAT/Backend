package yumyum.demo.src.community.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import yumyum.demo.config.Status;
import yumyum.demo.src.community.dto.*;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CommunityRepository {
    private final JdbcTemplate jdbcTemplate;

    public CommunityRepository(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void createPost(String username, PostDto postDto) {
       /* SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("post").usingGeneratedKeyColumns("post_id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("user_id", findUserIdByUsername(username));
        parameters.put("topic", topic);
        parameters.put("contents", contents);
        parameters.put("img_url", imgUrl);

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        return key.longValue();*/

        /*String query = "insert into post(user_id, topic, contents, img_url)" +
                "values(?, ? ,? ,? )";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = (connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(query, new String[]{"post_id"});
            preparedStatement.setLong(1, findUserIdByUsername(username));
            preparedStatement.setString(2, topic);
            preparedStatement.setString(3, contents);
            preparedStatement.setString(4, imgUrl);

            return preparedStatement;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().longValue();*/

        Object[] params = new Object[]{findUserIdByUsername(username), postDto.getTopic(), postDto.getContents(), postDto.getImgUrl(), "ACTIVE"};
        System.out.println(findUserIdByUsername(username));
        this.jdbcTemplate.update(
                "insert into post(user_id, topic, contents, img_url, status) values (?,?,?,?,?);",
                params
                );
    }

    public Long updatePost(Long post_id, PostDto postDto) {
        this.jdbcTemplate.update(
                "update post set topic = ?, contents = ?, img_url= ? where post_id = ? ",
                postDto.getTopic(), postDto.getContents(), postDto.getImgUrl(), post_id
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

    public Long createComment(String username, Long post_id, CommentDto commentDto) {

        //group number??? ?????? ????????? ???????????? 1???, ?????? ????????? ?????? ????????? group_number??? ?????? ??? (?????? ??? ??? + 1) ??? ??????
        String qurry = "insert into comment(group_number,comment_level, parent_id, post_id, user_id , contents, status)" +
                "values( if((select counting from(select count(*) as counting from comment where post_id = ?) c) = 0, 1, ((select num + 1  from (select max(group_number) as num from comment) as A) ) ),0, 0 , ? , ? , ?, ? )";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = (connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(qurry, new String[]{"comment_id"});
            preparedStatement.setLong(1,post_id);
            preparedStatement.setLong(2, post_id);
            preparedStatement.setLong(3, findUserIdByUsername(username));
            preparedStatement.setString(4, commentDto.getContents());
            preparedStatement.setString(5, "ACTIVE");

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

    public Long createReplyComment(String username, Long post_id, Long parent_id, CommentDto commentDto) {
        String qurry = "insert into comment(group_number, comment_level, parent_id, post_id, user_id, contents, status)" +
                "values((select groupNum from (select distinct group_number as groupNum from comment where comment_id = ?) as c), 1 , ? , ? , ? ,?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = (connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(qurry, new String[]{"comment_id"});
            preparedStatement.setLong(1, parent_id);
            preparedStatement.setLong(2, parent_id);
            preparedStatement.setLong(3, post_id);
            preparedStatement.setLong(4, findUserIdByUsername(username));
            preparedStatement.setString(5, commentDto.getContents());
            preparedStatement.setString(6, "ACTIVE");

            return preparedStatement;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().longValue();

    }

    //post_like table??? ??? ??????
    public void createPostLike(Long post_id, Long user_id){
        this.jdbcTemplate.update(
          "insert into post_like (post_id, user_id, status) values (?, ?, ?);",
                post_id, user_id, "ACTIVE"
        );
    }

    //post_lke table??? ??? ??????
    public void deletePostLike(Long post_id, Long user_id){
        this.jdbcTemplate.update(
                "update post_like set status = 0  where post_id = ?, user_id = ?;",
                post_id, user_id
        );
    }

    //post table??? count_like ??? ??????
    public void incresePostCountLike(Long post_id){
        this.jdbcTemplate.update(
                "update post set count_like = count_like + 1 where post_id = ?",
                post_id
        );
    }

    //post table count_like ??? ??????
    public void decresePostCountLike(Long post_id){
        this.jdbcTemplate.update(
                "update post set count_like = count_like - 1 where post_id = ?",
                post_id
        );
    }


    //post_like table??? ?????? ?????? ????????? ??????
    public Long countPostLike(Long post_id, Long user_id){
        Long count = this.jdbcTemplate.queryForObject("select count(*) from post_like where post_id = ? and user_id = ?"
                , Long.class, post_id, user_id);
        return count;
    }

    //post_like table??? status ??? ??????
    public String statusPostLike(Long post_id, Long user_id){
        String status  = this.jdbcTemplate.queryForObject("select status from post_like where post_id = ? and user_id = ?"
        , String.class, post_id, user_id);
        return status;
    }
   /* public int statusPostLike(Long post_id, Long user_id){
        int status = this.jdbcTemplate.queryForObject("select status from post_like where post_id = ? and user_id = ?",
                Integer.class, post_id, user_id);
        return status;
    }
*/
    //????????? ??????
    public void deletePost(Long post_id) {
        this.jdbcTemplate.update(
                "update post set status = 'INACTIVE' where post_id = ?",
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

    public void updateComment(Long comment_id, CommentDto commentDto) {
        this.jdbcTemplate.update(
                "update comment set contents = ? where comment_id = ? ;",
                commentDto.getContents(), comment_id
        );
    }

    public void deleteComment(Long comment_id) {
        this.jdbcTemplate.update(
                "update comment set status = 'INACTIVE'  where comment_id = ? ;",
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

    public void changeStatusPostLike(Long post_id, Long user_id, String status) {
        this.jdbcTemplate.update(
                "update post_like set status = ? where post_id = ? and user_id = ?;",
                status, post_id, user_id
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
                "insert into comment_like (user_id, comment_id, status) values (?, ?, ?);",
                user_id, comment_id, "ACTIVE"
        );
    }

    public void increseCommentCountLike(Long comment_id) {
        this.jdbcTemplate.update(
                "update comment set count_like = count_like + 1 where comment_id = ?;",
                comment_id
        );
    }

    public String statusCommentLike(Long user_id, Long comment_id) {
        String status = this.jdbcTemplate.queryForObject(
                "select status from comment_like where user_id = ? and comment_id = ?",
                String.class, user_id, comment_id
        );
        return status;
    }

    public void changeStatusCommentLike(Long user_id, Long comment_id, String status) {
        this.jdbcTemplate.update(
                "update comment_like set status = ? where user_id = ? and comment_id = ?;",
                status, user_id, comment_id
        );
    }

    public void decreseCommentCountLike(Long comment_id) {
        this.jdbcTemplate.update(
                "update comment set count_like = count_like - 1 where comment_id = ?;",
                comment_id
        );
    }


    public PostDto getPostScreen(Long post_id) {

        return this.jdbcTemplate.queryForObject(
                "select * from post outer join user on post.user_id = user.user_id where post.post_id = ?",
                (rs, rowNum) ->{
                    PostDto postDto = new PostDto();
                    
                    return postDto;
                }
                );

    }

   /* public PostinfoDto getpostInfo(Long post_id) {
        return this.jdbcTemplate.queryForObject(
                "select post.topic, post.contents, post.img_url, post.count_comment, post.create_at, user.profileImgulr, user.nickname from post outer join user on post.user_id = user.user_id where post.post_id = ?",
                (rs, rowNum) -> {
                    PostinfoDto postinfoDto = new PostinfoDto();
                    postinfoDto.setProfileImgUrl(rs.getString("user.profile_img_url"));
                    postinfoDto.setNickName(rs.getString("user.nickname"));
                    postinfoDto.setTopic(rs.getString("post.topic"));
                    postinfoDto.setContents(rs.getString("contents"));
                    postinfoDto.setImgUrl(rs.getString("post.img_url"));
                    postinfoDto.setCountPostLike(rs.getLong("post.count_like"));
                    postinfoDto.setCountComment(rs.getLong("post.count_comment"));
                    postinfoDto.setCrated_at(rs.getDate("created_At").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                    return postinfoDto;
                }, post_id);

    }*/


    public List<CommentInfoDto> getCommentInfo(Long post_id, Long user_id) {

        return this.jdbcTemplate.query(
                "select U.profile_img_url, U.nick_name, C.comment_id, C.contents, C.count_like, C.parent_id, C.group_number, C.created_at, C.updated_at, CL.comment_like_id,\n" +
                       "                       (\n" +
                       "                       case\n" +
                       "                       when timestampdiff(YEAR, C.created_at, now()) >= 1 then concat(timestampdiff(YEAR, C.created_at, now())  , '??? ???')\n" +
                       "                       when timestampdiff(MONTH, C.created_at, now()) >= 1 then concat(timestampdiff(MONTH, C.created_at, now()) , '??? ???')\n" +
                       "                       when timestampdiff(DAY, C.created_at, now()) >=1 then concat(timestampdiff(DAY, C.created_at, now()) , '??? ???')\n" +
                       "                       when timestampdiff(HOUR, C.created_at, now()) >=1 then concat(timestampdiff(HOUR, C.created_at, now()) , '?????? ???')\n" +
                       "                       when timestampdiff(MINUTE, C.created_at, now()) >= 1 then concat(timestampdiff(MINUTE, C.created_at, now()) , '??? ???')\n" +
                       "                       ELSE concat(timestampdiff(SECOND, C.created_at, now()), '??? ???')\n" +
                       "                       END\n" +
                       "                       ) as created_time,\n" +
                       "                        (\n" +
                       "                            case\n" +
                       "                                when C.created_at = C.updated_at then false\n" +
                       "                            ELSE true\n" +
                       "                            END\n" +
                       "                        ) as check_updated,\n" +
                                                "(if(CL.comment_like_id is null, false, true)) as check_my_like"+
                       "\n" +
                       "                       from user U\n" +
                       "                       " +
                        "Left join comment C on U.user_id = C.user_id\n" +
                        "left join comment_like CL on C.comment_id = CL.comment_id and CL.user_id = ? and CL.status = 'ACTIVE'"+
                       "                       where C.post_id = ?  ORDER BY C.group_number, C.created_at" ,
                (rs, rowNum) ->{
                    CommentInfoDto commentInfoDto = new CommentInfoDto();
                    commentInfoDto.setProfileImgUrl(rs.getString("U.profile_img_url"));
                    commentInfoDto.setNickName(rs.getString("U.nick_name"));
                    commentInfoDto.setContents(rs.getString("C.contents"));
                    commentInfoDto.setCountCommentLike(rs.getLong("C.count_like"));
                    commentInfoDto.setCreate_at(rs.getString("created_time"));
                    commentInfoDto.setParentId(rs.getLong("parent_id"));
                    commentInfoDto.setGroupNumber(rs.getInt("C.group_number"));
                    commentInfoDto.setCommentId(rs.getLong("C.comment_id"));
                    // ?????? false??? ????????? ??? ??? ?????????, ?????? true??? ????????? ????????????
                    commentInfoDto.setCheckUpdated(rs.getBoolean("check_updated"));
                    // ?????? false??? ???????????? ??? ??? ?????????, ?????? true??? ???????????? ??? ????????????
                    commentInfoDto.setMyLike(rs.getBoolean("check_my_like"));

                    return commentInfoDto;
                }, user_id, post_id
        );
    }

    /*public List<CommentInfoDto> getCommentInfo(Long post_id) {
        return this.jdbcTemplate.query(
                "select U.profile_img_url, U.nick_name, C.comment_id, C.contents, C.count_like, C.parent_id, C.group_number, C.created_at, C.updated_at, " +
                        "( " +
                        "case " +
                        "when timestampdiff(YEAR, now(), C.created_at) >= 1 then concat(timestampdiff(YEAR, now(), C.created_at)  , '??? ???') " +
                        "when timestampdiff(MONTH, now(), C.created_at) >= 1 then concat(timestampdiff(MONTH, now(), C.created_at) + '??? ???') " +
                        "when timestampdiff(DAY, now(), C.created_at) >=1 then concat(timestampdiff(DAY, now(), C.created_at) + '??? ???') " +
                        "when timestampdiff(HOUR, now(), C.created_at) >=1 then concat(timestampdiff(HOUR, now(), C.created_at) + '?????? ???') " +
                        "when timestampdiff(MINUTE, now(), C.created_at) >= 1 then concat(timestampdiff(MINUTE, now(), C.created_at) + '??? ???') " +
                        "ELSE concat(timestampdiff(SECOND, now(), C.created_at)) " +
                        "END" +
                        ") as created_time, " +
                        "from user U " +
                        "Left join comment C on U.user_id = C.user_id " +
                        "where C.post_id = ? " + "ORDER BY C.group_number, C.created_at" ,
                (rs, rowNum) ->{
                    CommentInfoDto commentInfoDto = new CommentInfoDto();
                    commentInfoDto.setProfileImgUrl(rs.getString("U.profile_img_url"));
                    commentInfoDto.setNickName(rs.getString("U.nick_name"));
                    commentInfoDto.setContents(rs.getString("C.contents"));
                    commentInfoDto.setCountCommentLike(rs.getLong("C.count_like"));
                    commentInfoDto.setCreate_at(rs.getString("created_time"));
                    commentInfoDto.setParentId(rs.getLong("parent_id"));
                    commentInfoDto.setGroupNumber(rs.getInt("C.group_number"));
                    commentInfoDto.setCommentId(rs.getLong("C.comment_id"));

                    return commentInfoDto;
                }, post_id
        );
    }*/

    public PostinfoDto getPostInfo(Long post_id, Long user_id) {

        return this.jdbcTemplate.queryForObject(
                "select P.post_id, P.user_id, P.topic, P.contents, P.img_url, P.count_comment, P.count_like,P.created_at, U.profile_img_url, U.nick_name,\n" +
                        "                               (\n" +
                        "                                   case\n" +
                        "                                    when timestampdiff(YEAR, P.created_at, now()) >= 1 then concat(timestampdiff(YEAR, P.created_at, now())  , '??? ???')\n" +
                        "                                    when timestampdiff(MONTH, P.created_at, now()) >= 1 then concat(timestampdiff(MONTH, P.created_at, now()) , '??? ???')\n" +
                        "                                    when timestampdiff(DAY, P.created_at, now()) >=1 then concat(timestampdiff(DAY, P.created_at, now()) , '??? ???')\n" +
                        "                                    when timestampdiff(HOUR, P.created_at, now()) >=1 then concat(timestampdiff(HOUR, P.created_at, now()) , '?????? ???')\n" +
                        "                                    when timestampdiff(MINUTE, P.created_at, now()) >= 1 then concat(timestampdiff(MINUTE, P.created_at, now()) , '??? ???')\n" +
                        "                                    ELSE concat(timestampdiff(SECOND, P.created_at, now()), '??? ???')\n" +
                        "                                       END\n" +
                        "\n" +
                        "                                ) as created_time,\n" +
                        "                        ( exists (select pl.post_id from post_like where pl.user_id = ? and pl.status = 'ACTIVE')) as check_my_like\n" +
                        "                        from user U\n" +
                        "                        Left join post P on U.user_id = P.user_id\n" +
                        "                        LEFT JOIN post_like pl on P.post_id = pl.post_id\n" +
                        "\n" +
                        "                        where P.post_id = ?;",
                (rs, rowNum) -> {
                    PostinfoDto postinfoDto = new PostinfoDto();
                    postinfoDto.setPostId(rs.getLong("P.post_id"));
                    postinfoDto.setTopic(rs.getString("P.topic"));
                    postinfoDto.setContents(rs.getString("P.contents"));
                    postinfoDto.setImgUrl(rs.getString("P.img_url"));
                    postinfoDto.setCountComment(rs.getLong("P.count_comment"));
                    postinfoDto.setCountPostLike(rs.getLong("P.count_like"));
                    postinfoDto.setProfileImgUrl(rs.getString("U.profile_img_url"));
                    postinfoDto.setNickName(rs.getString("U.nick_name"));
                    postinfoDto.setCrated_at(rs.getString("created_time"));
                    postinfoDto.setMyLike(rs.getBoolean("check_my_like"));

                    return postinfoDto;
                },user_id, post_id
        );

    }

    public Long countGroupNumber(Long post_id){
        return this.jdbcTemplate.queryForObject(
                "select count(distinct group_number) from comment where post_id = ?", Long.class, post_id
        );
    }

    public Long checkGroupNumber(Long post_id, int group_number){
        return this.jdbcTemplate.queryForObject(
                "select count(distinct group_number) from comment where post_id = ? and group_number = ?",
                Long.class, post_id, group_number

        );
    }

    public List<CommunityMainDto> getCommunityScreen() {
        return this.jdbcTemplate.query(
                "select U.nick_name,P.post_id, P.topic, P.contents, P.img_url, P.count_like, P.count_comment,\n" +
                        "       (\n" +
                        "           case\n" +
                        "                                   when timestampdiff(YEAR, P.created_at, now()) >= 1 then concat(timestampdiff(YEAR, P.created_at, now())  , '??? ???')\n" +
                        "                                    when timestampdiff(MONTH, P.created_at, now()) >= 1 then concat(timestampdiff(MONTH, P.created_at, now()) , '??? ???')\n" +
                        "                                    when timestampdiff(DAY, P.created_at, now()) >=1 then concat(timestampdiff(DAY, P.created_at, now()) , '??? ???')\n" +
                        "                                    when timestampdiff(HOUR, P.created_at, now()) >=1 then concat(timestampdiff(HOUR, P.created_at, now()) , '?????? ???')\n" +
                        "                                    when timestampdiff(MINUTE, P.created_at, now()) >= 1 then concat(timestampdiff(MINUTE, P.created_at, now()) , '??? ???')\n" +
                        "                                    ELSE concat(timestampdiff(SECOND, P.created_at, now()) , '??? ???')\n" +
                        "                                       END\n" +
                        "\n" +
                        "           ) as created_time\n" +
                        "            from user U\n" +
                        "            LEFT JOIN post P on U.user_id = P.user_id\n" +
                        "            ORDER BY P.created_at",
                (rs, rowNum) -> {
                    CommunityMainDto communityMainDto = new CommunityMainDto();
                    communityMainDto.setNickName(rs.getString("U.nick_name"));
                    communityMainDto.setPostId(rs.getLong("P.post_id"));
                    communityMainDto.setTopic(rs.getString("P.topic"));
                    communityMainDto.setContents(rs.getString("P.contents"));
                    communityMainDto.setImgUrl(rs.getString("P.img_url"));
                    communityMainDto.setCountPostLike(rs.getLong("P.count_like"));
                    communityMainDto.setCountComment(rs.getLong("P.count_comment"));
                    communityMainDto.setCreated_at(rs.getString("created_time"));

                    return communityMainDto;
                }

        );
    }
}

