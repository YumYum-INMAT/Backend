package yumyum.demo.src.restaurant.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import yumyum.demo.config.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "search")
@NoArgsConstructor
@DynamicInsert
public class SearchEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_id")
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(30)")
    private String word;

}
