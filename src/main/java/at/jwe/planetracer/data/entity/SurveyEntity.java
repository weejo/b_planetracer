package at.jwe.planetracer.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "result")
@Table(name = "result", schema = "planetracer", indexes = {
        @Index(name = "levelid", columnList = "levelid")
})
public class SurveyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "surveyId")
    private Long surveyId;

    @Column(name ="answer1")
    private String answer1;

    @Column(name ="answer2")
    private String answer2;

    @Column(name ="answer3")
    private String answer3;

}
