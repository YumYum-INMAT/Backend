package yumyum.demo.src.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yumyum.demo.src.restaurant.dto.PopularSearchWordDto;
import yumyum.demo.src.restaurant.entity.SearchEntity;

import java.util.List;

public interface SearchRepository extends JpaRepository<SearchEntity, Long> {
    @Query(value = "select word, rank() over (order by cnt desc) as ranking from (select word, count(search_id) as cnt from searchgroup by word) as countWord", nativeQuery = true)
    List<PopularSearchWordDto> getPopularSearchWord();
}
