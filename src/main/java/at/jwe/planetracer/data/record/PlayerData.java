package at.jwe.planetracer.data.record;

import at.jwe.planetracer.data.record.input.InputData;

public record PlayerData(Long pathLength, int[] clusterData, InputData inputData) {
}
