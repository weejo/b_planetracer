package at.jwe.planetracer.data.record;

import java.util.List;

public record MapData(String name, Long squareSize, Long initialTime, Double decay, List<DataPoint> dataPoints, Long resolutionX, Long resolutionY) {
}
