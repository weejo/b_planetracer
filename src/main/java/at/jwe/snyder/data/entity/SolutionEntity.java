package at.jwe.snyder.data.entity;

import io.hypersistence.utils.hibernate.type.array.IntArrayType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "solution")
@Table(name = "solution", schema = "snyder", indexes = {
        @Index(name = "levelid", columnList = "levelid")
})
public class SolutionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer solutionId;

    @Column(name = "levelid")
    private Integer levelId;

    private int cutoff;

    @Column(name = "created")
    private LocalDateTime created;

    @Type(
            value = IntArrayType.class,
            parameters = @org.hibernate.annotations.Parameter(
                    name = "sql_array_type",
                    value = "int"
            )
    )
    @Column(name = "aggregatedsolution",
            columnDefinition = "int[][]")
    private int[][] aggregatedsolution;
}
