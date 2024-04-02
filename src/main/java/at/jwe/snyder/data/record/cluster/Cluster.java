package at.jwe.snyder.data.record.cluster;

import at.jwe.snyder.data.record.data.DataPoint;

import java.util.List;

public record Cluster(List<DataPoint> dataPointList, Double value) {
}
