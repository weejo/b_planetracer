package at.jwe.snyder.data.record.data;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MapConfig {
    private int highestX;
    private int highestY;
    private int changeX;
    private int changeY;
    private double decay;
    private List<DataPoint> dataPoints;
}
