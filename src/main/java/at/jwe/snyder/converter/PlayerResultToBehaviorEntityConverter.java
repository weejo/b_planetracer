package at.jwe.snyder.converter;

import at.jwe.snyder.data.entity.BehaviorEntity;
import at.jwe.snyder.data.record.PlayerResult;
import org.springframework.stereotype.Component;

@Component
public class PlayerResultToBehaviorEntityConverter {

    public BehaviorEntity convert(PlayerResult playerResult, int resultId) {
        return BehaviorEntity.builder()
                .resultID(resultId)
                .countUp(playerResult.playerData().inputData().countUp())
                .timeUp(playerResult.playerData().inputData().timeUp())
                .countLeft(playerResult.playerData().inputData().countLeft())
                .timeLeft(playerResult.playerData().inputData().timeLeft())
                .countRight(playerResult.playerData().inputData().countRight())
                .timeRight(playerResult.playerData().inputData().timeRight())
                .countSpace(playerResult.playerData().inputData().countSpace())
                .timeSpace(playerResult.playerData().inputData().timeSpace())
                .build();
    }
}
