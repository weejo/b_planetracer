package at.jwe.data.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "HIGHSCOREENTRY")
@Table(name = "HIGHSCOREENTRY", indexes = {
        @Index(name = "LEVEL_ID", columnList ="LEVEL_ID")
})
public class HighscoreEntity {
    @Id
    private Long highscoreId;

    @Column(name = "LEVELID")
    private Long levelId;

    @Column(name = "POINTS")
    private int points;

    @Column(name = "NAME")
    private String name;
}
