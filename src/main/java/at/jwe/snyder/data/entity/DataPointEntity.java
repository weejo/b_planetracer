package at.jwe.snyder.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "datapoint")
@Table(name = "datapoint", schema="snyder")
public class DataPointEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="level_levelid")
    private LevelEntity levelentity;

    @Basic
    private int external;

    private int x;

    private int y;
}
