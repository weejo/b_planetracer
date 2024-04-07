package at.jwe.snyder.data.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "highscore")
@Table(name = "highscore", schema="snyder", indexes = {
        @Index(name = "levelid", columnList ="levelid")
})
public class HighscoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="HIGHSCOREID")
    private Integer highscoreId;

    @Column(name = "LEVELID")
    private Integer levelId;

    @Column(name = "POINTS")
    private int points;

    @Column(name = "NAME")
    private String name;
}
