package yumyum.demo.src.restaurant.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import yumyum.demo.src.restaurant.dto.PopularSearchWordDto;

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
}
