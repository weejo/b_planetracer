package at.jwe.snyder.data.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "solutiondatapoint")
@Table(name = "solutiondatapoint", schema = "snyder")
public class SolutionDataPointEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solution_id")
    private SolutionEntity solutionEntity;

    private int external;

    private int x;

    private int y;

    private int value;
}