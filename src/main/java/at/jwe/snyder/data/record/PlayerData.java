package at.jwe.snyder.data.record;

import at.jwe.snyder.data.record.cluster.Cluster;
import at.jwe.snyder.data.record.input.InputData;

public record PlayerData(int pathLength, Cluster[] clusterData, InputData inputData) {
}
