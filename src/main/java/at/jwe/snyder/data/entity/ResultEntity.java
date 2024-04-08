package at.jwe.snyder.data.entity;

import io.hypersistence.utils.hibernate.type.array.IntArrayType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

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

    @Type(
            value = IntArrayType.class,
            parameters = @org.hibernate.annotations.Parameter(
                    name = "sql_array_type",
                    value = "int"
            )
    )
    @Column(name = "result",
            columnDefinition = "int[][]")
    private int[][] result;

    @Column(name = "score")
    private int score;

    @Column(name = "pathlength")
    private int pathLength;
}
