package at.jwe.planetracer.service;

import at.jwe.planetracer.data.record.DataPoint;
import at.jwe.planetracer.data.record.MapData;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ComputationRunnable implements Runnable {
    private final List<Double> cellValues = new ArrayList<>();

    private final MapData mapData;
    private final long startRow;
    private final long endRow;

    ComputationRunnable(MapData mapData, long startRow, long endRow) {
        this.mapData = mapData;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    private Double getDist(long x, long y, DataPoint dataPoint) {
        return Math.sqrt(((dataPoint.y() - y) * (dataPoint.y() - y)) + ((dataPoint.x() - x) * (dataPoint.x() - x)));
    }

    @Override
    public void run() {
        System.out.println("Thread " + Thread.currentThread().getId() + " started!");
        for (long y = startRow; y < endRow; y++) {
            for (long x = 0; x < mapData.resolutionX(); x++) {

                double cellValue = 0.0;

                for (DataPoint dataPoint : mapData.dataPoints()) {
                    // x -> exp(-beta*dist(x, x_0)
                    cellValue += Math.exp(-mapData.decay() * getDist(x, y, dataPoint));
                }
                cellValues.add(cellValue);
            }
        }
        System.out.println("Thread " + Thread.currentThread().getId() + " finished!");
    }
}
