package at.jwe.snyder.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "result")
@Table(name = "result", schema = "snyder", indexes = {
        @Index(name = "levelid", columnList = "levelid")
})
public class ResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resultid")
    private Integer resultId;

    @Column(name = "levelid")
    private Integer levelId;

    @Column(name = "result")
    private int[] result;

    @Column(name ="score")
    private int score;

    @Column(name ="pathlength")
    private int pathLength;
}
