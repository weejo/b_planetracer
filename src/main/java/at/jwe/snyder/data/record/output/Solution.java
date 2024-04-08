package at.jwe.snyder.data.record.output;

import java.time.LocalDateTime;

public record Solution(Integer solutionÍd, Integer levelId, int cutoffValue, LocalDateTime createdAt,
                       int[][] solutionMatrix) {
}
