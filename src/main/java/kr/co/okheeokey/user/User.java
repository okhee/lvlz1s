package kr.co.okheeokey.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private Boolean isAlive;

    @Builder
    public User(String name, Boolean isAlive) {
        this.name = name;
        this.isAlive = isAlive;
    }
}
