package at.jwe.snyder.service;

import at.jwe.snyder.data.entity.BehaviorEntity;
import at.jwe.snyder.data.entity.HighscoreEntity;
import at.jwe.snyder.data.entity.LevelEntity;
import at.jwe.snyder.data.entity.ResultEntity;
import at.jwe.snyder.data.record.LevelOverview;
import at.jwe.snyder.data.record.OverviewLevel;
import at.jwe.snyder.data.record.PlayerResult;
import at.jwe.snyder.data.record.cluster.ClusterResult;
import at.jwe.snyder.data.record.data.MapConfig;
import at.jwe.snyder.data.record.data.MapData;
import at.jwe.snyder.data.record.highscore.Highscore;
import at.jwe.snyder.data.record.highscore.HighscoreEntry;
import at.jwe.snyder.data.record.level.LayerInfo;
import at.jwe.snyder.data.record.level.Level;
import at.jwe.snyder.data.record.level.Tileset;
import at.jwe.snyder.repository.BehaviorRepository;
import at.jwe.snyder.repository.HighscoreRepository;
import at.jwe.snyder.repository.LevelRepository;
import at.jwe.snyder.repository.ResultRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class DataServiceImpl implements DataService {

    private final LevelRepository levelRepository;

    private final ResultRepository resultRepository;

    private final HighscoreRepository highscoreRepository;

    private final BehaviorRepository behaviorRepository;

    private final MapConfigService mapConfigService;


    @Override
    public Level addLevel(MapData mapData) {
        log.atInfo().log("Adding Map!");
        Optional<LevelEntity> existing = levelRepository.findByName(mapData.name());
        if (existing.isPresent()) {
            return getLevelOutput(existing.get());
        }

        MapConfig mapConfig = mapConfigService.computeMapConfig(mapData);

        List<Double> cellValues = generateCellValues(mapConfig);

        List<Integer> resultList = normalizeAndCleanCellValues(cellValues);

        LevelEntity level = persistLevelEntity(mapData, mapConfig, resultList);

        return getLevelOutput(level);
    }

    private LevelEntity persistLevelEntity(MapData mapData, MapConfig mapConfig, List<Integer> resultList) {
        LevelEntity level = LevelEntity.builder()
                .name(mapData.name() != null ? mapData.name() : "NoName" + Math.random())
                .height(mapConfig.getHighestY())
                .width(mapConfig.getHighestX())
                .data(resultList.stream().mapToInt(i -> i).toArray())
                .build();

        levelRepository.save(level);
        return level;
    }

    private List<Double> generateCellValues(MapConfig mapConfig) {
        List<Double> cellValues;
        try {
            cellValues = computeCellValues(mapConfig);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return cellValues;
    }


    private static Level getLevelOutput(LevelEntity level) {
        return new Level(List.of(new LayerInfo("base_layer", level.getData(), level.getHeight(), 1, "tilelayer", true, level.getWidth(), 0, 0)),
                "orthogonal",
                8,
                8,
                level.getHeight(),
                level.getWidth(),
                List.of(new Tileset(0, "tileset.png", 16, 176, 0, "backgroundtileset", 0, 16, 16)));
    }

    private List<Double> computeCellValues(MapConfig mapConfig) throws InterruptedException {
        List<Double> cellValues;

        long floor = mapConfig.getHighestY() / 4;

        ComputationRunnable run1 = new ComputationRunnable(mapConfig, 0, floor);
        ComputationRunnable run2 = new ComputationRunnable(mapConfig, floor, 2 * floor);
        ComputationRunnable run3 = new ComputationRunnable(mapConfig, 2 * floor, 3 * floor);
        ComputationRunnable run4 = new ComputationRunnable(mapConfig, 3 * floor, mapConfig.getHighestY());

        Thread thread1 = new Thread(run1);
        Thread thread2 = new Thread(run2);
        Thread thread3 = new Thread(run3);
        Thread thread4 = new Thread(run4);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        thread1.join();
        cellValues = new ArrayList<>(run1.getCellValues());
        thread2.join();
        cellValues.addAll(run2.getCellValues());
        thread3.join();
        cellValues.addAll(run3.getCellValues());
        thread4.join();
        cellValues.addAll(run4.getCellValues());

        return cellValues;
    }

    private List<Integer> normalizeAndCleanCellValues(List<Double> cellValues) {
        OptionalDouble max = cellValues.stream().mapToDouble(v -> v).max();
        List<Integer> resultList = new ArrayList<>();
        for (Double v : cellValues) {
            resultList.add((int) Math.floor((v / max.getAsDouble()) * 10));
        }
        return resultList;
    }


    @Override
    public ClusterResult getClusters(Long levelId, Long threshold) {

        return null;
    }

    @Override
    public Highscore getHighscore(Long levelId) {
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
        Long levelId = playerResult.levelId();
        ResultEntity resultEntity = ResultEntity.builder()
                .levelId(playerResult.levelId())
                .result(playerResult.playerData().clusterData())
                .pathLength(playerResult.playerData().pathLength())
                .score(playerResult.entry().points())
                .build();

        ResultEntity save = resultRepository.save(resultEntity);

        BehaviorEntity behaviorEntity = BehaviorEntity.builder()
                .resultID(save.getResultId())
                .countUp(playerResult.playerData().inputData().countUp())
                .timeUp(playerResult.playerData().inputData().timeUp())
                .countLeft(playerResult.playerData().inputData().countLeft())
                .timeLeft(playerResult.playerData().inputData().timeLeft())
                .countRight(playerResult.playerData().inputData().countRight())
                .timeRight(playerResult.playerData().inputData().timeRight())
                .countSpace(playerResult.playerData().inputData().countSpace())
                .timeSpace(playerResult.playerData().inputData().timeSpace())
                .build();

        behaviorRepository.save(behaviorEntity);

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
    public Level getLevelData(Long levelId) {
        Optional<LevelEntity> levelEntity = levelRepository.findById(levelId);

        if (levelEntity.isEmpty()) {
            throw new RuntimeException("Level with given ID:" + levelId + " not found!");
        }

        return getLevelOutput(levelEntity.get());

    }

    @Override
    public LevelOverview getLevelOverview() {
        List<LevelEntity> allLevels = levelRepository.findAll();
        if (allLevels.isEmpty()) {
            return null;
        }
        List<OverviewLevel> overviewList = new ArrayList<>();

        for (LevelEntity level : allLevels) {
            overviewList.add(new OverviewLevel(level.getName(), level.getWidth().toString() + "x" + level.getHeight().toString(), level.getLevelId()));
        }
        return new LevelOverview(overviewList);
    }
}
