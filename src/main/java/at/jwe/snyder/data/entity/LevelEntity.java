package at.jwe.snyder.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "level")
@Table(name = "level", schema = "snyder", indexes = {
        @Index(name = "levelname", columnList = "name")
})
public class LevelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "levelid")
    private Integer levelId;

    @Column(name = "name")
    private String name;

    @Column(name = "width")
    private int width;

    @Column(name = "height")
    private int height;

    @Column(name = "changex")
    private int changeX;
    @Column(name = "changey")
    private int changeY;

    @Column(name = "playerx")
    private int playerX;

    @Column(name = "playery")
    private int playerY;

    @Column(name = "data")
    private int[] data;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "levelentity")
    private List<DataPointEntity> realDataPoints = new ArrayList();

}
