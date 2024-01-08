package at.jwe.planetracer.data.record.data;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MapConfig {
    private Long highestX;
    private Long highestY;
    private double decay;
    private List<DataPoint> dataPoints;
}
