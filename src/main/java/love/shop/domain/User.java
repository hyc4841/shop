package love.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String name;
    private Integer age;
    private String memo;

    public User() {
    }

    public User(String name, Integer age, String memo) {
        this.name = name;
        this.age = age;
        this.memo = memo;
    }
}