package at.jwe.snyder.converter;

import at.jwe.snyder.data.entity.DataPointEntity;
import at.jwe.snyder.data.entity.LevelEntity;
import at.jwe.snyder.data.entity.ResultEntity;
import at.jwe.snyder.data.record.PlayerResult;
import at.jwe.snyder.data.record.cluster.Cluster;
import at.jwe.snyder.repository.LevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PlayerResultToResultEntityConverter {

    private final LevelRepository levelRepository;

    public ResultEntity convert(PlayerResult playerResult) {
        Optional<LevelEntity> optionalLevel = levelRepository.findById(playerResult.levelId());
        if (optionalLevel.isEmpty()) {
            throw new RuntimeException("Level not found!");
        }

        int[][] adjacencyMatrix = calculateAdjacencyMatrix(optionalLevel, playerResult.playerData().clusterData());

        return ResultEntity.builder()
                .levelId(playerResult.levelId())
                .result(adjacencyMatrix)
                .pathLength(playerResult.playerData().pathLength())
                .score(playerResult.entry().points())
                .build();
    }

    private int[][] calculateAdjacencyMatrix(Optional<LevelEntity> optionalLevel, Cluster[] clusters) {
        LevelEntity levelEntity = optionalLevel.get();
        List<DataPointEntity> realDataPoints = levelEntity.getRealDataPoints();

        int[][] adjacencyMatrix = new int[realDataPoints.size()][realDataPoints.size()];

        int width = levelEntity.getWidth();

        List<DataPointEntity> toBeHandledDatapoints = realDataPoints;

        for (Cluster cluster : clusters) {
            List<Integer> ids = new ArrayList<>();
            Integer score = cluster.score();

            for (Integer position : cluster.points()) {
                int x = position % width;
                int y = position / width;
                List<DataPointEntity> toBeExcluded = new ArrayList<>();

                for (DataPointEntity realDataPoint : toBeHandledDatapoints) {
                    if ((realDataPoint.getX() + levelEntity.getChangeX()) == x
                            && (realDataPoint.getY() + levelEntity.getChangeY()) == y) {
                        toBeExcluded.add(realDataPoint);
                        ids.add(realDataPoint.getExternal());
                    }
                }
                toBeHandledDatapoints.removeAll(toBeExcluded);
            }
            if (!ids.isEmpty()) {
                addToAdjacencyMatrix(ids, adjacencyMatrix, score);
            }
        }
        return adjacencyMatrix;
    }

    private static void addToAdjacencyMatrix(List<Integer> ids, int[][] adjacencyMatrix, Integer score) {
        List<Integer> otherIds = ids.stream().map(Integer::new).collect(Collectors.toList());
        for (Integer id : ids) {
            for (Integer otherId : otherIds) {
                adjacencyMatrix[id][otherId] = score;
                adjacencyMatrix[otherId][id] = score;
            }
            otherIds.remove(id);
        }
    }

}
