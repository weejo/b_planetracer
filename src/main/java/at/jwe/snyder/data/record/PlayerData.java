package at.jwe.snyder.data.record;

import at.jwe.snyder.data.record.input.InputData;

public record PlayerData(Long pathLength, int[] clusterData, InputData inputData) {
}
