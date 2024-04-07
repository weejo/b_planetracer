package at.jwe.snyder.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "solutionEntity")
    private List<SolutionDataPointEntity> realDataPoints = new ArrayList();
}
