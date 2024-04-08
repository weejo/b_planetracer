package at.jwe.snyder.service;

import at.jwe.snyder.data.entity.*;
import at.jwe.snyder.repository.LevelRepository;
import at.jwe.snyder.repository.ResultRepository;
import at.jwe.snyder.repository.SolutionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class SolutionService {

    private final LevelRepository levelRepository;

    private final ResultRepository resultRepository;

    private final SolutionRepository solutionRepository;


    @Transactional
    public SolutionEntity computeSolution(int levelId, float cutoff) {

        Optional<LevelEntity> optionalLevel = levelRepository.findById(levelId);
        if (optionalLevel.isEmpty()) {
            throw new RuntimeException("Level for solution does not exist!");
        }

        List<ResultEntity> allByLevelId = resultRepository.findAllByLevelId(levelId);

        int cutOffValue = calculateCutOff(allByLevelId, cutoff);

        List<ResultEntity> filteredEntries = allByLevelId.stream().filter(o -> o.getScore() >= cutOffValue).collect(Collectors.toList());

        if (filteredEntries.isEmpty()) {
            throw new RuntimeException("No solutions exist!");
        }

        SolutionEntity solutionEntity = SolutionEntity.builder()
                .cutoff(cutOffValue).levelId(levelId)
                .aggregatedsolution(calculateSolution(filteredEntries, optionalLevel.get().getRealDataPoints().size()))
                .created(LocalDateTime.now())
                .build();
        return solutionRepository.save(solutionEntity);
    }

    private int[][] calculateSolution(List<ResultEntity> filteredEntries, int size) {
        int[][] result = new int[size][size];
        for (ResultEntity filteredEntry : filteredEntries) {
            for (int y = 0; y < filteredEntry.getResult().length; y++) {
                for (int x = 0; x < filteredEntry.getResult().length; x++) {
                    result[x][y] += filteredEntry.getResult()[x][y];
                }
            }
        }
        return result;
    }


    private int calculateCutOff(List<ResultEntity> allByLevelId, float cutoff) {

        int highest = 0;

        for (ResultEntity resultEntity : allByLevelId) {
            if (resultEntity.getScore() > highest) {
                highest = resultEntity.getScore();
            }
        }
        return (int) (highest * cutoff);
    }
}
