package kr.co.okheeokey.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private Long isAlive = 1L;

    @Builder
    public User(String name, Long isAlive) {
        this.name = name;
        this.isAlive = isAlive;
    }
}
