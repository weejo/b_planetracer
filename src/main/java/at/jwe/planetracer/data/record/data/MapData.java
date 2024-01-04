package at.jwe.planetracer.data.record.data;

import java.util.List;

public record MapData(String name, Long initialTime, Double decay, List<DataPoint> dataPoints) {
}
