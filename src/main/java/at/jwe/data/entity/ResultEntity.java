package at.jwe.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Blob;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "RESULTENTRY")
@Table(name = "RESULTENTRY", indexes = {
        @Index(name = "LEVEL_ID", columnList ="LEVEL_ID")
})
public class ResultEntity {
    @Id
    private Long resultId;

    @Column(name = "LEVELID")
    private Long levelId;

    @Lob
    private Blob result;
}
