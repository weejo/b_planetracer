package at.jwe.snyder.service;

import at.jwe.snyder.data.entity.*;
import at.jwe.snyder.repository.LevelRepository;
import at.jwe.snyder.repository.ResultRepository;
import at.jwe.snyder.repository.SolutionDataPointRepository;
import at.jwe.snyder.repository.SolutionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private final SolutionDataPointRepository solutionDataPointRepository;


    @Transactional
    public void computeSolution(int levelId, float cutoff) {

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
        SolutionEntity solutionEntity = SolutionEntity.builder().cutoff(cutOffValue).levelId(levelId).created(LocalDateTime.now()).build();
        SolutionEntity save = solutionRepository.save(solutionEntity);

        createSolutionData(optionalLevel.get(), filteredEntries, save);
    }

    private void createSolutionData(LevelEntity levelEntity, List<ResultEntity> filteredEntries, SolutionEntity save) {
        List<DataPointEntity> realDataPoints = levelEntity.getRealDataPoints();
        int width = levelEntity.getWidth();


        List<SolutionDataPointEntity> solutionPoints = new ArrayList<>();

        for (DataPointEntity realDataPoint : realDataPoints) {
            int x = realDataPoint.getX() + levelEntity.getChangeX();
            int y = realDataPoint.getY() + levelEntity.getChangeY();
            int value = 0;

            //x is x, y has to be aligned to the array.
            int position = (width * y) + x;

            for (ResultEntity filteredEntry : filteredEntries) {
                value += filteredEntry.getResult()[position];
            }
            solutionPoints.add(SolutionDataPointEntity.builder()
                    .solutionEntity(save)
                    .external(realDataPoint.getExternal())
                    .x(realDataPoint.getX())
                    .y(realDataPoint.getY())
                    .value(value)
                    .build());
        }
        solutionDataPointRepository.saveAll(solutionPoints);
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
