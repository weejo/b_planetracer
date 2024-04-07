package at.jwe.snyder.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "behavior")
@Table(name = "behavior", schema="snyder", indexes = {
        @Index(name = "resultid", columnList ="resultid")
})
public class BehaviorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="behaviorid")
    private Integer behaviorId;

    @Column(name = "resultid")
    private Integer resultID;

    @Column(name ="countup")
    private int countUp;

    @Column(name ="timeup")
    private int timeUp;

    @Column(name ="countleft")
    private int countLeft;

    @Column(name ="timeleft")
    private int timeLeft;

    @Column(name ="countright")
    private int countRight;

    @Column(name ="timeright")
    private int timeRight;

    @Column(name ="countspace")
    private int countSpace;

    @Column(name ="timespace")
    private int timeSpace;
}
