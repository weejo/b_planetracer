package at.jwe.snyder.data.record.data;

import java.util.List;

public record MapData(String name, Double decay, List<DataPoint> dataPoints) {
}
