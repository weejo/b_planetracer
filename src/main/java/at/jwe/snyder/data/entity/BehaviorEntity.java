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
    private Long behaviorId;

    @Column(name = "resultid")
    private Long resultID;

    @Column(name ="countup")
    private Long countUp;

    @Column(name ="timeup")
    private Long timeUp;

    @Column(name ="countleft")
    private Long countLeft;

    @Column(name ="timeleft")
    private Long timeLeft;

    @Column(name ="countright")
    private Long countRight;

    @Column(name ="timeright")
    private Long timeRight;

    @Column(name ="countspace")
    private Long countSpace;

    @Column(name ="timespace")
    private Long timeSpace;
}
