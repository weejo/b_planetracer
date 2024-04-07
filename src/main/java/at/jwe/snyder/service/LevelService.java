package at.jwe.snyder.service;

import at.jwe.snyder.converter.DataPointToDataEntityConverter;
import at.jwe.snyder.data.entity.DataPointEntity;
import at.jwe.snyder.data.entity.LevelEntity;
import at.jwe.snyder.data.record.LevelOverview;
import at.jwe.snyder.data.record.OverviewLevel;
import at.jwe.snyder.data.record.data.DataPoint;
import at.jwe.snyder.data.record.data.MapConfig;
import at.jwe.snyder.data.record.data.MapData;
import at.jwe.snyder.data.record.level.LayerInfo;
import at.jwe.snyder.data.record.level.Level;
import at.jwe.snyder.data.record.level.Tileset;
import at.jwe.snyder.repository.DataPointRepository;
import at.jwe.snyder.repository.LevelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class LevelService {

    private final LevelRepository levelRepository;

    private final DataPointRepository dataPointRepository;

    private final DataPointToDataEntityConverter dataPointToDataEntityConverter;

    private final MapConfigService mapConfigService;

    public Level addLevel(MapData mapData) {
        Optional<LevelEntity> existing = levelRepository.findByName(mapData.name());
        if (existing.isPresent()) {

            createAndSaveDataPoints(mapData.dataPoints(), existing.get());

            return getLevelOutput(existing.get());
        }
        List<DataPoint> dataPoints = mapData.dataPoints().stream().map( o -> new DataPoint(o.getX(), o.getY(), o.getId())).collect(Collectors.toList());

        MapConfig mapConfig = mapConfigService.computeMapConfig(mapData);

        List<Double> cellValues = generateCellValues(mapConfig);

        List<Integer> resultList = normalizeAndCleanCellValues(cellValues);

        LevelEntity level = persistLevelEntity(mapData, mapConfig, resultList);
        createAndSaveDataPoints(dataPoints, level);

        return getLevelOutput(level);
    }

    private void createAndSaveDataPoints(List<DataPoint> dataPoints, LevelEntity levelEntity) {
        List<DataPointEntity> dataPointEntities = new ArrayList<>();
        for (DataPoint dataPoint : dataPoints) {
            dataPointEntities.add(dataPointToDataEntityConverter.convert(dataPoint, levelEntity));
        }
        dataPointRepository.saveAll(dataPointEntities);
    }

    private LevelEntity persistLevelEntity(MapData mapData, MapConfig mapConfig, List<Integer> resultList) {
        LevelEntity level = LevelEntity.builder()
                .name(mapData.name() != null ? mapData.name() : "NoName" + Math.random())
                .height(mapConfig.getHighestY())
                .width(mapConfig.getHighestX())
                .changeX(mapConfig.getChangeX())
                .changeY(mapConfig.getChangeY())
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

    private List<Double> computeCellValues(MapConfig mapConfig) throws InterruptedException {
        List<Double> cellValues;

        int floor = mapConfig.getHighestY() / 4;

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

    private Level getLevelOutput(LevelEntity level) {
        return new Level(List.of(new LayerInfo("base_layer", level.getData(), level.getHeight(), 1, "tilelayer", true, level.getWidth(), 0, 0)),
                "orthogonal",
                8,
                8,
                level.getHeight(),
                level.getWidth(),
                List.of(new Tileset(0, "tileset.png", 16, 176, 0, "backgroundtileset", 0, 16, 16)));
    }

    public Level getLevelData(int levelId) {

        Optional<LevelEntity> levelEntity = levelRepository.findById(levelId);

        if (levelEntity.isEmpty()) {
            throw new RuntimeException("Level with given ID:" + levelId + " not found!");
        }

        return getLevelOutput(levelEntity.get());

    }

    public LevelOverview getLevelOverview() {
        List<LevelEntity> allLevels = levelRepository.findAll();
        if (allLevels.isEmpty()) {
            return null;
        }
        List<OverviewLevel> overviewList = new ArrayList<>();

        for (LevelEntity level : allLevels) {
            overviewList.add(new OverviewLevel(level.getName(), level.getWidth()+ "x" + level.getHeight(), level.getLevelId()));
        }
        return new LevelOverview(overviewList);
    }
}
