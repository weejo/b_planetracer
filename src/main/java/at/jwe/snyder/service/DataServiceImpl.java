package at.jwe.snyder.service;

import at.jwe.snyder.converter.PlayerResultToBehaviorEntityConverter;
import at.jwe.snyder.converter.PlayerResultToResultEntityConverter;
import at.jwe.snyder.data.entity.HighscoreEntity;
import at.jwe.snyder.data.entity.ResultEntity;
import at.jwe.snyder.data.record.LevelOverview;
import at.jwe.snyder.data.record.PlayerResult;
import at.jwe.snyder.data.record.data.MapData;
import at.jwe.snyder.data.record.highscore.Highscore;
import at.jwe.snyder.data.record.highscore.HighscoreEntry;
import at.jwe.snyder.data.record.level.Level;
import at.jwe.snyder.repository.BehaviorRepository;
import at.jwe.snyder.repository.HighscoreRepository;
import at.jwe.snyder.repository.ResultRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class DataServiceImpl implements DataService {

    private final LevelService levelService;

    private final ResultRepository resultRepository;

    private final HighscoreRepository highscoreRepository;

    private final BehaviorRepository behaviorRepository;

    private final SolutionService solutionService;

    private final PlayerResultToResultEntityConverter playerResultToResultEntityConverter;

    private final PlayerResultToBehaviorEntityConverter playerResultToBehaviorEntityConverter;


    @Override
    public Level addLevel(MapData mapData) {
        log.atInfo().log("Adding Map!");
        return levelService.addLevel(mapData);
    }

    @Override
    public void computeSolution(int levelId, float cutoff) {
        solutionService.computeSolution(levelId, cutoff);
    }

    @Override
    public Highscore getHighscore(int levelId) {
        List<HighscoreEntity> allByLevelId = highscoreRepository.findAllByLevelId(levelId);
        allByLevelId.sort(Comparator.comparingInt(HighscoreEntity::getPoints).reversed());
        List<HighscoreEntry> highscoreEntryList = new ArrayList<>();
        for (HighscoreEntity highscoreEntity : allByLevelId) {
            highscoreEntryList.add(new HighscoreEntry(highscoreEntity.getPoints(), highscoreEntity.getName()));
        }
        return new Highscore(highscoreEntryList);
    }

    @Override
    public void addResult(PlayerResult playerResult) {
        int levelId = playerResult.levelId();

        ResultEntity save = resultRepository.save(playerResultToResultEntityConverter.convert(playerResult));

        behaviorRepository.save(playerResultToBehaviorEntityConverter.convert(playerResult, save.getResultId()));

        List<HighscoreEntity> allByLevelId = highscoreRepository.findAllByLevelId(levelId);
        allByLevelId.sort(Comparator.comparingInt(HighscoreEntity::getPoints).reversed());

        HighscoreEntity newScore = HighscoreEntity.builder()
                .points(playerResult.entry().points())
                .name(playerResult.entry().name())
                .levelId(levelId)
                .build();

        if (allByLevelId.size() < 5) {
            highscoreRepository.save(newScore);
        } else {
            HighscoreEntity last = allByLevelId.get(allByLevelId.size() - 1);
            if (playerResult.entry().points() > last.getPoints()) {
                highscoreRepository.save(newScore);
                highscoreRepository.delete(last);
            }
        }
    }

    @Override
    public Level getLevelData(int levelId) {
        return levelService.getLevelData(levelId);
    }

    @Override
    public LevelOverview getLevelOverview() {
        return levelService.getLevelOverview();
    }
}
