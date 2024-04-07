package at.jwe.snyder.service;

import at.jwe.snyder.data.record.data.DataPoint;

public class PointUtil {
    public static Double getDist(int x, int y, DataPoint dataPoint) {
        return Math.abs(Math.sqrt(((dataPoint.getY() - y) * (dataPoint.getY() - y)) + ((dataPoint.getX() - x) * (dataPoint.getX() - x))));
    }
}

