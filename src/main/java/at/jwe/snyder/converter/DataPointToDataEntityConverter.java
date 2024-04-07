package at.jwe.snyder.converter;

import at.jwe.snyder.data.entity.DataPointEntity;
import at.jwe.snyder.data.entity.LevelEntity;
import at.jwe.snyder.data.record.data.DataPoint;
import org.springframework.stereotype.Component;

@Component
public class DataPointToDataEntityConverter {

    public DataPointEntity convert(DataPoint dataPoint, LevelEntity levelEntity) {
        return DataPointEntity.builder()
                .x(dataPoint.getX())
                .y(dataPoint.getY())
                .external(dataPoint.getId())
                .levelentity(levelEntity)
                .build();
    }
}
