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
    private final int startRow;
    private final int endRow;

    ComputationRunnable(MapConfig mapConfig, int startRow, int endRow) {
        this.mapConfig = mapConfig;
        this.startRow = startRow;
        this.endRow = endRow;
    }



    @Override
    public void run() {
        System.out.println("Thread " + Thread.currentThread().getId() + " started!");
        for (int y = startRow; y < endRow; y++) {
            for (int x = 0; x < mapConfig.getHighestX(); x++) {

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
