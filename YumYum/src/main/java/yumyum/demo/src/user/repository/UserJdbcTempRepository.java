package yumyum.demo.src.user.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import yumyum.demo.src.community.dto.CommunityMainDto;
import yumyum.demo.src.user.dto.MyHeartRestaurantDto;
import yumyum.demo.src.user.dto.MyReviewDto;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserJdbcTempRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public Long findUserIdByUsername(String username) {
        return this.jdbcTemplate.queryForObject(
                "select user_id from user where username = ?",
                Long.class, username);
    }

    public List<CommunityMainDto> getPost(Long user_id) {
        return this.jdbcTemplate.query(
                "select U.nick_name,P.post_id, P.topic, P.contents, P.img_url, P.count_like, P.count_comment,\n" +
                        "       (\n" +
                        "           case\n" +
                        "                                   when timestampdiff(YEAR, P.created_at, now()) >= 1 then concat(timestampdiff(YEAR, P.created_at, now())  , '년 전')\n" +
                        "                                    when timestampdiff(MONTH, P.created_at, now()) >= 1 then concat(timestampdiff(MONTH, P.created_at, now()) , '월 전')\n" +
                        "                                    when timestampdiff(DAY, P.created_at, now()) >=1 then concat(timestampdiff(DAY, P.created_at, now()) , '일 전')\n" +
                        "                                    when timestampdiff(HOUR, P.created_at, now()) >=1 then concat(timestampdiff(HOUR, P.created_at, now()) , '시간 전')\n" +
                        "                                    when timestampdiff(MINUTE, P.created_at, now()) >= 1 then concat(timestampdiff(MINUTE, P.created_at, now()) , '분 전')\n" +
                        "                                    ELSE concat(timestampdiff(SECOND, P.created_at, now()) , '초 전')\n" +
                        "                                       END\n" +
                        "\n" +
                        "           ) as created_time\n" +
                        "            from post P\n" +
                        "            inner JOIN user U on U.user_id = P.user_id\n" +
                        "            where P.status = 'ACTIVE' and P.user_id = ?\n" +
                        "            ORDER BY P.created_at desc",
                (rs, rowNum) -> {
                    CommunityMainDto communityMainDto = new CommunityMainDto();
                    communityMainDto.setNickName(rs.getString("U.nick_name"));
                    communityMainDto.setPostId(rs.getLong("P.post_id"));
                    communityMainDto.setTopic(rs.getString("P.topic"));
                    communityMainDto.setContents(rs.getString("P.contents"));
                    communityMainDto.setImgUrl(rs.getString("P.img_url"));
                    communityMainDto.setCountPostLike(rs.getLong("P.count_like"));
                    communityMainDto.setCountComment(rs.getLong("P.count_comment"));
                    communityMainDto.setCreatedAt(rs.getString("created_time"));

                    return communityMainDto;
                }, user_id

        );
    }

    public List<MyHeartRestaurantDto> getMyHeartRestaurant(Long user_id) {
    return this.jdbcTemplate.query(
            "select H.heart_id, H.user_id, R.restaurant_id, R.profile_img_url, R.restaurant_name, R.average_star, R.address, R.count_heart, R.restaurant_type\n" +
                    "from restaurant R\n" +
                    "left join heart H on R.restaurant_id = H.restaurant_id\n" +
                    "where H.user_id = ? and R.status = 'ACTIVE' and H.status = 'ACTIVE'\n" +
                    "order by H.created_at desc;",
            (rs, rowNum) -> {
                MyHeartRestaurantDto myHeartRestaurantDto = new MyHeartRestaurantDto();
                myHeartRestaurantDto.setHeartId(rs.getLong("H.heart_id"));
                myHeartRestaurantDto.setUserId(rs.getLong("H.user_id"));
                myHeartRestaurantDto.setRestaurantId(rs.getLong("R.restaurant_id"));
                myHeartRestaurantDto.setImgUrl(rs.getString("R.profile_img_url"));
                //myHeartRestaurantDto.setImgUrl(rs.getString("R.img_url"));
                myHeartRestaurantDto.setRestaurantName(rs.getString("R.restaurant_name")); //getcursorname? 찾아보기
                myHeartRestaurantDto.setAverageStar(rs.getDouble("R.average_star"));
                myHeartRestaurantDto.setAddress(rs.getString("R.address"));
                myHeartRestaurantDto.setCountHeart(rs.getInt("R.count_heart"));
                myHeartRestaurantDto.setRestaurantType(rs.getString("R.restaurant_type"));

                return myHeartRestaurantDto;
            }, user_id
    );

    }
}
