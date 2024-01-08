package at.jwe.planetracer.data.record.cluster;

import at.jwe.planetracer.data.record.data.DataPoint;

import java.util.List;

public record Cluster(List<DataPoint> dataPointList, Double value) {
}
