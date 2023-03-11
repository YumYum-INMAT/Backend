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

}
