package yumyum.demo.src.restaurant.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import yumyum.demo.src.restaurant.dto.PopularSearchWordDto;
import yumyum.demo.src.restaurant.dto.RestaurantDto;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class RestaurantJdbcTempRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public RestaurantJdbcTempRepository(DataSource dataSource) {jdbcTemplate = new JdbcTemplate(dataSource);}


    public void postSearch(String contents) {
        this.jdbcTemplate.update(
                "insert into search(status, word) values (?,?);",
                "ACTIVE", contents
        );
    }

    public List<PopularSearchWordDto> getPopularSearchWord() {
        return this.jdbcTemplate.query(
                "select word, rank() over (order by cnt desc) as ranking\n" +
                        "from (select word, count(search_id) as cnt\n" +
                        "from search\n" +
                        "group by word) as countWord",
                (rs, rowNum) -> {
                    PopularSearchWordDto popularSearchWordDto = new PopularSearchWordDto();
                    popularSearchWordDto.setWord(rs.getString("word"));
                    popularSearchWordDto.setRanking(rs.getInt("ranking"));

                    return popularSearchWordDto;
                }
        );

    }

    public Long findUserIdByUsername(String username){
        Long userId = this.jdbcTemplate.queryForObject(
                "select user_id from user where username = ?",
                Long.class, username
        );
        return userId;
    }

    /*public boolean checkRestaurantHeart(Long restaurantId, String username){
        Long userId = findUserIdByUsername(username);

        return this.jdbcTemplate.queryForObject(
                "select exists(\n" +
                        "    select heart_id\n" +
                        "    from heart h\n" +
                        "    left join restaurant r on r.restaurant_id = h.restaurant_id\n" +
                        "    where h.status = 'ACTIVE' and h.restaurant_id = ? and h.user_id = ?\n" +
                        "           );",
                Boolean.class, restaurantId, userId
        );
    }*/

    public List<RestaurantDto> getSearchResult1(Long userId, String query) {
    return this.jdbcTemplate.query(
            "select distinct r.restaurant_id, r.profile_img_url, r.restaurant_name, r.address, r.average_star, r.count_review, r.average_price, r.complexity, r.restaurant_type,\n" +
                    "    exists(select h2.heart_id from heart h2 where h2.status='ACTIVE' and h2.restaurant_id = r.restaurant_id and h2.user_id= ?) as user_heart\n" +
                    "from restaurant r\n" +
                    "inner join restaurant_menu rm on r.restaurant_id = rm.restaurant_id\n" +
                    "where r.restaurant_name LIKE concat('%', ? ,'%') or rm.menu_name LIKE concat('%', ? ,'%')\n" +
                    "order by r.average_star, r.count_review desc  ;",
            (rs, rowNum) -> {
                return new RestaurantDto(
                        rs.getLong("r.restaurant_id"),
                        rs.getString("r.profile_img_url"),
                        rs.getString("r.restaurant_name"),
                        rs.getString("r.address"),
                        rs.getDouble("r.average_star"),
                        rs.getInt("r.count_review"),
                        rs.getInt("r.average_price"),
                        rs.getInt("r.complexity"),
                        rs.getString("r.restaurant_type"),
                        rs.getBoolean("user_heart")
                );
            }, userId, query, query
    );
    }

    public List<RestaurantDto> getSearchResult2(Long userId, String query) {
        return this.jdbcTemplate.query(
                "select distinct r.restaurant_id, r.profile_img_url, r.restaurant_name, r.address, r.average_star, r.count_review, r.average_price, r.complexity, r.restaurant_type,\n" +
                        "    exists(select h2.heart_id from heart h2 where h2.status='ACTIVE' and h2.restaurant_id = r.restaurant_id and h2.user_id= ?) as user_heart\n" +
                        "from restaurant r\n" +
                        "inner join restaurant_menu rm on r.restaurant_id = rm.restaurant_id\n" +
                        "where r.restaurant_name LIKE concat('%', ? ,'%') or rm.menu_name LIKE concat('%', ? ,'%')\n" +
                        "order by r.average_price  ;",
                (rs, rowNum) -> {
                    return new RestaurantDto(
                            rs.getLong("r.restaurant_id"),
                            rs.getString("r.profile_img_url"),
                            rs.getString("r.restaurant_name"),
                            rs.getString("r.address"),
                            rs.getDouble("r.average_star"),
                            rs.getInt("r.count_review"),
                            rs.getInt("r.average_price"),
                            rs.getInt("r.complexity"),
                            rs.getString("r.restaurant_type"),
                            rs.getBoolean("user_heart")
                    );
                }, userId, query, query
        );
    }

    public List<RestaurantDto> getSearchResult3(Long userId, String query) {
        return this.jdbcTemplate.query(
                "select distinct r.restaurant_id, r.profile_img_url, r.restaurant_name, r.address, r.average_star, r.count_review, r.average_price, r.complexity, r.restaurant_type,\n" +
                        "    exists(select h2.heart_id from heart h2 where h2.status='ACTIVE' and h2.restaurant_id = r.restaurant_id and h2.user_id= ?) as user_heart\n" +
                        "from restaurant r\n" +
                        "inner join restaurant_menu rm on r.restaurant_id = rm.restaurant_id\n" +
                        "where r.restaurant_name LIKE concat('%', ? ,'%') or rm.menu_name LIKE concat('%', ? ,'%')\n" +
                        "order by r.average_price desc  ;",
                (rs, rowNum) -> {
                    return new RestaurantDto(
                            rs.getLong("r.restaurant_id"),
                            rs.getString("r.profile_img_url"),
                            rs.getString("r.restaurant_name"),
                            rs.getString("r.address"),
                            rs.getDouble("r.average_star"),
                            rs.getInt("r.count_review"),
                            rs.getInt("r.average_price"),
                            rs.getInt("r.complexity"),
                            rs.getString("r.restaurant_type"),
                            rs.getBoolean("user_heart")
                    );
                }, userId, query, query
        );
    }

    public List<RestaurantDto> getSearchResult4(Long userId, String query) {
        return this.jdbcTemplate.query(
                "select distinct r.restaurant_id, r.profile_img_url, r.restaurant_name, r.address, r.average_star, r.count_review, r.average_price, r.complexity, r.restaurant_type,\n" +
                        "    exists(select h2.heart_id from heart h2 where h2.status='ACTIVE' and h2.restaurant_id = r.restaurant_id and h2.user_id= ?) as user_heart\n" +
                        "from restaurant r\n" +
                        "inner join restaurant_menu rm on r.restaurant_id = rm.restaurant_id\n" +
                        "where r.restaurant_name LIKE concat('%', ? ,'%') or rm.menu_name LIKE concat('%', ? ,'%')\n" +
                        "order by r.count_heart desc  ;",
                (rs, rowNum) -> {
                    return new RestaurantDto(
                            rs.getLong("r.restaurant_id"),
                            rs.getString("r.profile_img_url"),
                            rs.getString("r.restaurant_name"),
                            rs.getString("r.address"),
                            rs.getDouble("r.average_star"),
                            rs.getInt("r.count_review"),
                            rs.getInt("r.average_price"),
                            rs.getInt("r.complexity"),
                            rs.getString("r.restaurant_type"),
                            rs.getBoolean("user_heart")
                    );
                }, userId, query, query
        );
    }

    public List<RestaurantDto> getSearchResult5(Long userId, String query) {
        return this.jdbcTemplate.query(
                "select distinct r.restaurant_id, r.profile_img_url, r.restaurant_name, r.address, r.average_star, r.count_review, r.average_price, r.complexity, r.restaurant_type,\n" +
                        "    exists(select h2.heart_id from heart h2 where h2.status='ACTIVE' and h2.restaurant_id = r.restaurant_id and h2.user_id= ?) as user_heart\n" +
                        "from restaurant r\n" +
                        "inner join restaurant_menu rm on r.restaurant_id = rm.restaurant_id\n" +
                        "where r.restaurant_name LIKE concat('%', ? ,'%') or rm.menu_name LIKE concat('%', ? ,'%')\n" +
                        "order by r.count_heart desc  ;",
                (rs, rowNum) -> {
                    return new RestaurantDto(
                            rs.getLong("r.restaurant_id"),
                            rs.getString("r.profile_img_url"),
                            rs.getString("r.restaurant_name"),
                            rs.getString("r.address"),
                            rs.getDouble("r.average_star"),
                            rs.getInt("r.count_review"),
                            rs.getInt("r.average_price"),
                            rs.getInt("r.complexity"),
                            rs.getString("r.restaurant_type"),
                            rs.getBoolean("user_heart")
                    );
                }, userId, query, query
        );
    }

}
