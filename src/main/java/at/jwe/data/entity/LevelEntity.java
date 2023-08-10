package at.jwe.data.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "LEVEL")
@Table(name = "LEVEL", indexes = {
        @Index(name = "LEVELNAME", columnList = "NAME")
})
public class LevelEntity {
    @Id
    private Long levelId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "MAXDISTANCE")
    private Long maxDistance;

    @Column(name = "INITIALTIME")
    private Long initialTime;

    @Column(name = "LEVELX")
    private Long levelX;

    @Column(name = "LEVELY")
    private Long levelY;

    @Column(name = "WIDTH")
    private Long width;

    @Column(name = "HEIGHT")
    private Long height;

    @Column(name = "OBJECTS")
    private List<Object> objects;
}
