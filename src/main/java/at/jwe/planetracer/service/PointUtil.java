package at.jwe.planetracer.service;

import at.jwe.planetracer.data.record.data.DataPoint;

public class PointUtil {
    public static Double getDist(long x, long y, DataPoint dataPoint) {
        return Math.abs(Math.sqrt(((dataPoint.getY() - y) * (dataPoint.getY() - y)) + ((dataPoint.getX() - x) * (dataPoint.getX() - x))));
    }
}

