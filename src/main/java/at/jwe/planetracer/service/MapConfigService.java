package at.jwe.planetracer.service;

import at.jwe.planetracer.data.record.data.DataPoint;
import at.jwe.planetracer.data.record.data.MapConfig;
import at.jwe.planetracer.data.record.data.MapData;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class MapConfigService {
    private static final int MARGIN = 50;

    public MapConfig computeMapConfig(MapData mapData) {
        MapConfig mapConfig = getMapConfig(mapData);
        mapConfig.setDecay(mapData.decay());

        computeChanges(mapConfig);

        transformData(mapConfig);
        return mapConfig;
    }

    private void transformData(MapConfig mapConfig) {
        for (DataPoint dataPoint : mapConfig.getDataPoints()) {
            dataPoint.setX(dataPoint.getX() + mapConfig.getChangeX());
            dataPoint.setY(dataPoint.getY() + mapConfig.getChangeY());
        }
    }

    private void computeChanges(MapConfig mapConfig) {
        if (mapConfig.getChangeX() <= 0) {
            mapConfig.setChangeX(Math.abs(mapConfig.getChangeX()) + MARGIN);
        } else {
            mapConfig.setChangeX(MARGIN - mapConfig.getChangeX());
        }

        if (mapConfig.getChangeY() <= 0) {
            mapConfig.setChangeY(Math.abs(mapConfig.getChangeY()) + MARGIN);
        } else {
            mapConfig.setChangeY(MARGIN - mapConfig.getChangeY());
        }

        mapConfig.setHighestX(mapConfig.getHighestX() + mapConfig.getChangeX());
        mapConfig.setHighestY(mapConfig.getHighestY() + mapConfig.getChangeY());
    }

    private MapConfig getMapConfig(MapData mapData) {
        MapConfig mapConfig = MapConfig.builder()
                .changeX(Long.MAX_VALUE)
                .changeY(Long.MAX_VALUE)
                .highestX(Long.MIN_VALUE)
                .highestY(Long.MIN_VALUE)
                .dataPoints(mapData.dataPoints())
                .build();

        for (DataPoint dataPoint : mapData.dataPoints()) {
            if (dataPoint.getX() < mapConfig.getChangeX()) {
                mapConfig.setChangeX(dataPoint.getX());
            }
            if (dataPoint.getX() > mapConfig.getHighestX()) {
                mapConfig.setHighestX(dataPoint.getX());
            }
            if (dataPoint.getY() < mapConfig.getChangeY()) {
                mapConfig.setChangeY(dataPoint.getY());
            }
            if (dataPoint.getY() > mapConfig.getHighestY()) {
                mapConfig.setHighestY(dataPoint.getY());
            }

        }
        return mapConfig;
    }

}
