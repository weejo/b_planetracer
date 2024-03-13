package at.jwe.planetracer.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "level")
@Table(name = "level", schema = "planetracer", indexes = {
        @Index(name = "levelname", columnList = "name")
})
public class LevelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "levelid")
    private Long levelId;

    @Column(name = "name")
    private String name;

    @Column(name = "width")
    private Long width;

    @Column(name = "height")
    private Long height;

    @Column(name = "playerx")
    private Long playerX;

    @Column(name = "playery")
    private Long playerY;

    @Column(name = "data")
    private int[] data;
}
