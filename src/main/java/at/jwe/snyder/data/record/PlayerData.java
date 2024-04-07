package at.jwe.snyder.data.record;

import at.jwe.snyder.data.record.input.InputData;

public record PlayerData(int pathLength, int[] clusterData, InputData inputData) {
}
