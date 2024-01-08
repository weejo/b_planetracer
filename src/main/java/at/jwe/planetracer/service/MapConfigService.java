package at.jwe.planetracer.service;

import at.jwe.planetracer.data.record.data.DataPoint;
import at.jwe.planetracer.data.record.data.MapConfig;
import at.jwe.planetracer.data.record.data.MapData;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class MapConfigService {
    private static final Long MARGIN = 250L;
    private Long changeX= Long.MAX_VALUE;
    private Long changeY = Long.MAX_VALUE;

    public MapConfig computeMapConfig(MapData mapData) {
        MapConfig mapConfig = getMapConfig(mapData);
        mapConfig.setDecay(mapData.decay());

        computeChanges(mapConfig);

        transformData(mapConfig);
        return mapConfig;
    }

    private void transformData(MapConfig mapConfig) {
        for (DataPoint dataPoint : mapConfig.getDataPoints()) {
            dataPoint.setX(dataPoint.getX() + changeX);
            dataPoint.setY(dataPoint.getY() + changeY);
        }
    }

    /**
     * This function computes how much we have to move a single point so it fits into phasers coordinate system.
     *
     * @param mapConfig the map config
     */
    private void computeChanges(MapConfig mapConfig) {
        // (-100, -200) changeX = +350, changeY = +450
        // (-100, 200) changeX = +350, changeY = 50
        // (100, 200) changeX = 150, changeY = 50
        // (400, 300) changeX = -150, changeY = -50

        // ChangeX/Y are currently just the lowest positon in the data.
        // so -100 needs to be moved by + 100
        if(changeX <= 0) {
            changeX = -changeX;
            changeX += MARGIN;
        } else {
            if (changeX <= MARGIN) {
                changeX = MARGIN - changeX;
            } else {
                changeX = -(changeX - MARGIN);
            }
        }

        if (changeY <= 0) {
            changeY = -changeY;
            changeY += MARGIN;
        } else {
            if (changeY <= MARGIN) {
                changeY += MARGIN - changeY;
            } else {
                changeY = -(changeY - MARGIN);
            }
        }

        // Properly set highestX and highestY with the now known changeX/Y values.
        mapConfig.setHighestX(mapConfig.getHighestX() + changeX);
        mapConfig.setHighestY(mapConfig.getHighestY() + changeY);
    }

    private MapConfig getMapConfig(MapData mapData) {
        // initialise default values
        MapConfig mapConfig = MapConfig.builder()
                .highestX(Long.MIN_VALUE)
                .highestY(Long.MIN_VALUE)
                .dataPoints(mapData.dataPoints())
                .build();

        //compute all currently possible values (change + highest)
        for (DataPoint dataPoint : mapData.dataPoints()) {
            if (dataPoint.getX() < changeX) {
                changeX = dataPoint.getX();
            }
            if (dataPoint.getX() > mapConfig.getHighestX()) {
                mapConfig.setHighestX(dataPoint.getX());
            }
            if (dataPoint.getY() < changeX) {
                changeY = dataPoint.getY();
            }
            if (dataPoint.getY() > mapConfig.getHighestY()) {
                mapConfig.setHighestY(dataPoint.getY());
            }

        }
        return mapConfig;
    }

}
