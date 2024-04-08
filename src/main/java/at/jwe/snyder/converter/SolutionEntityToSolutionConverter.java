package at.jwe.snyder.converter;

import at.jwe.snyder.data.entity.SolutionEntity;
import at.jwe.snyder.data.record.output.Solution;
import org.springframework.stereotype.Component;

@Component
public class SolutionEntityToSolutionConverter {

    public Solution convert(SolutionEntity solutionEntity) {
        return new Solution(solutionEntity.getSolutionId(), solutionEntity.getLevelId(), solutionEntity.getCutoff(), solutionEntity.getCreated(), solutionEntity.getAggregatedsolution());
    }
}
