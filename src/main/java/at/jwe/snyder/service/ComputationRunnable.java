package at.jwe.snyder.service;

import at.jwe.snyder.data.record.data.DataPoint;
import at.jwe.snyder.data.record.data.MapConfig;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static at.jwe.snyder.service.PointUtil.getDist;

@Getter
public class ComputationRunnable implements Runnable {
    private final List<Double> cellValues = new ArrayList<>();

    private final MapConfig mapConfig;
    private final long startRow;
    private final long endRow;

    ComputationRunnable(MapConfig mapConfig, long startRow, long endRow) {
        this.mapConfig = mapConfig;
        this.startRow = startRow;
        this.endRow = endRow;
    }



    @Override
    public void run() {
        System.out.println("Thread " + Thread.currentThread().getId() + " started!");
        for (long y = startRow; y < endRow; y++) {
            for (long x = 0; x < mapConfig.getHighestX(); x++) {

                double cellValue = 0.0;

                for (DataPoint dataPoint : mapConfig.getDataPoints()) {
                    // x -> exp(-beta*dist(x, x_0)
                    cellValue += Math.exp(-mapConfig.getDecay() * getDist(x, y, dataPoint));
                }
                cellValues.add(cellValue);
            }
        }
        System.out.println("Thread " + Thread.currentThread().getId() + " finished!");
    }
}
