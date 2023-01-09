package yumyum.demo.src.restaurant.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import yumyum.demo.src.restaurant.dto.PopularSearchWordDto;
import yumyum.demo.src.restaurant.dto.RestaurantDto;
import yumyum.demo.src.restaurant.dto.SearchRestaurantDto;

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
                "select word, count(search_id) as cnt\n" +
                        "from search\n" +
                        "group by word\n" +
                        "order by cnt desc\n" +
                        "limit 10;",
                (rs, rowNum) -> {
                    PopularSearchWordDto popularSearchWordDto = new PopularSearchWordDto();
                    popularSearchWordDto.setWord(rs.getString("word"));
                    popularSearchWordDto.setCountWord(rs.getLong("cnt"));

                    return popularSearchWordDto;
                }
        );

    }

    public List<SearchRestaurantDto> getSearchResult1(String search) {
    return this.jdbcTemplate.query(
            "select distinct r.restaurant_id, r.profile_img_url, r.restaurant_name, r.address, r.average_star, r.count_review, r.average_price\n" +
                    "from restaurant r\n" +
                    "inner join restaurant_menu rm on r.restaurant_id = rm.restaurant_id\n" +
                    "where r.restaurant_name LIKE concat('%', ? ,'%') or rm.menu_name LIKE concat('%',?,'%')\n" +
                    "order by r.average_star, r.count_review desc  ;",
            (rs, rowNum) -> {
                SearchRestaurantDto searchRestaurantDto = new SearchRestaurantDto();
                searchRestaurantDto.setRestaurantId(rs.getLong("restaurant_id"));
                searchRestaurantDto.setProfileImgUrl(rs.getString("profile_img_url"));
                searchRestaurantDto.setRestaurantName(rs.getString("restaurant_name"));
                searchRestaurantDto.setAddress(rs.getString("address"));
                searchRestaurantDto.setAverageStar(rs.getDouble("average_star"));
                searchRestaurantDto.setCountReview(rs.getInt("count_review"));
                searchRestaurantDto.setAveragePrice(rs.getInt("average_price"));
                return searchRestaurantDto;
            }, search, search
    );
    }

    public List<SearchRestaurantDto> getSearchRestul2(String search) {
        return this.jdbcTemplate.query(
                "select distinct r.restaurant_id, r.profile_img_url, r.restaurant_name, r.address, r.average_star, r.count_review, r.average_price\n" +
                        "from restaurant r\n" +
                        "inner join restaurant_menu rm on r.restaurant_id = rm.restaurant_id\n" +
                        "where r.restaurant_name LIKE concat('%', ? ,'%') or rm.menu_name LIKE concat('%',?,'%')\n" +
                        "order by r.average_price  ;",
                (rs, rowNum) -> {
                    SearchRestaurantDto searchRestaurantDto = new SearchRestaurantDto();
                    searchRestaurantDto.setRestaurantId(rs.getLong("restaurant_id"));
                    searchRestaurantDto.setProfileImgUrl(rs.getString("profile_img_url"));
                    searchRestaurantDto.setRestaurantName(rs.getString("restaurant_name"));
                    searchRestaurantDto.setAddress(rs.getString("address"));
                    searchRestaurantDto.setAverageStar(rs.getDouble("average_star"));
                    searchRestaurantDto.setCountReview(rs.getInt("count_review"));
                    searchRestaurantDto.setAveragePrice(rs.getInt("average_price"));
                    return searchRestaurantDto;
                }, search, search
        );
    }

    public List<SearchRestaurantDto> getSearchRestul3(String search) {
        return this.jdbcTemplate.query(
                "select distinct r.restaurant_id, r.profile_img_url, r.restaurant_name, r.address, r.average_star, r.count_review, r.average_price\n" +
                        "from restaurant r\n" +
                        "inner join restaurant_menu rm on r.restaurant_id = rm.restaurant_id\n" +
                        "where r.restaurant_name LIKE concat('%', ? ,'%') or rm.menu_name LIKE concat('%',?,'%')\n" +
                        "order by r.average_price desc ;",
                (rs, rowNum) -> {
                    SearchRestaurantDto searchRestaurantDto = new SearchRestaurantDto();
                    searchRestaurantDto.setRestaurantId(rs.getLong("restaurant_id"));
                    searchRestaurantDto.setProfileImgUrl(rs.getString("profile_img_url"));
                    searchRestaurantDto.setRestaurantName(rs.getString("restaurant_name"));
                    searchRestaurantDto.setAddress(rs.getString("address"));
                    searchRestaurantDto.setAverageStar(rs.getDouble("average_star"));
                    searchRestaurantDto.setCountReview(rs.getInt("count_review"));
                    searchRestaurantDto.setAveragePrice(rs.getInt("average_price"));
                    return searchRestaurantDto;
                }, search, search
        );
    }

    public List<SearchRestaurantDto> getSearchRestul4(String search) {
        return this.jdbcTemplate.query(
                "select distinct r.restaurant_id, r.profile_img_url, r.restaurant_name, r.address, r.average_star, r.count_review, r.average_price\n" +
                        "from restaurant r\n" +
                        "inner join restaurant_menu rm on r.restaurant_id = rm.restaurant_id\n" +
                        "where r.restaurant_name LIKE concat('%', ? ,'%') or rm.menu_name LIKE concat('%',?,'%')\n" +
                        "order by r.count_review desc ;",
                (rs, rowNum) -> {
                    SearchRestaurantDto searchRestaurantDto = new SearchRestaurantDto();
                    searchRestaurantDto.setRestaurantId(rs.getLong("restaurant_id"));
                    searchRestaurantDto.setProfileImgUrl(rs.getString("profile_img_url"));
                    searchRestaurantDto.setRestaurantName(rs.getString("restaurant_name"));
                    searchRestaurantDto.setAddress(rs.getString("address"));
                    searchRestaurantDto.setAverageStar(rs.getDouble("average_star"));
                    searchRestaurantDto.setCountReview(rs.getInt("count_review"));
                    searchRestaurantDto.setAveragePrice(rs.getInt("average_price"));
                    return searchRestaurantDto;
                }, search, search
        );
    }

    public List<SearchRestaurantDto> getSearchRestul5(String search) {
        return this.jdbcTemplate.query(
                "select distinct r.restaurant_id, r.profile_img_url, r.restaurant_name, r.address, r.average_star, r.count_review, r.average_price\n" +
                        "from restaurant r\n" +
                        "inner join restaurant_menu rm on r.restaurant_id = rm.restaurant_id\n" +
                        "where r.restaurant_name LIKE concat('%', ? ,'%') or rm.menu_name LIKE concat('%',?,'%')\n" +
                        "order by r.count_heart desc ;",
                (rs, rowNum) -> {
                    SearchRestaurantDto searchRestaurantDto = new SearchRestaurantDto();
                    searchRestaurantDto.setRestaurantId(rs.getLong("restaurant_id"));
                    searchRestaurantDto.setProfileImgUrl(rs.getString("profile_img_url"));
                    searchRestaurantDto.setRestaurantName(rs.getString("restaurant_name"));
                    searchRestaurantDto.setAddress(rs.getString("address"));
                    searchRestaurantDto.setAverageStar(rs.getDouble("average_star"));
                    searchRestaurantDto.setCountReview(rs.getInt("count_review"));
                    searchRestaurantDto.setAveragePrice(rs.getInt("average_price"));
                    return searchRestaurantDto;
                }, search, search
        );
    }
}
