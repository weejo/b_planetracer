package at.jwe.snyder.converter;

import at.jwe.snyder.data.entity.ResultEntity;
import at.jwe.snyder.data.record.PlayerResult;
import org.springframework.stereotype.Component;

@Component
public class PlayerResultToResultEntityConverter {

    public ResultEntity convert(PlayerResult playerResult) {
        return ResultEntity.builder()
                .levelId(playerResult.levelId())
                .result(playerResult.playerData().clusterData())
                .pathLength(playerResult.playerData().pathLength())
                .score(playerResult.entry().points())
                .build();
    }

}
