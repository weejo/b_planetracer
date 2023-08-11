package at.jwe.planetracer.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Blob;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "result")
@Table(name = "result", schema="planetracer", indexes = {
        @Index(name = "levelid", columnList ="levelid")
})
public class ResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="resultid")
    private Long resultId;

    @Column(name = "levelid")
    private Long levelId;

    @Column(name ="result")
    private String result;
}
