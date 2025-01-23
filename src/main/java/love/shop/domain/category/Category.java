package love.shop.domain.category;

import jakarta.persistence.*;

import lombok.Getter;

@Entity
@Getter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;



}
