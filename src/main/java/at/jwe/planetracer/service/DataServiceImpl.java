package at.jwe.planetracer.service;

import at.jwe.planetracer.data.entity.LevelEntity;
import at.jwe.planetracer.data.record.LevelOverview;
import at.jwe.planetracer.data.record.MapData;
import at.jwe.planetracer.data.record.OverviewLevel;
import at.jwe.planetracer.data.record.PlayerResult;
import at.jwe.planetracer.data.record.cluster.ClusterResult;
import at.jwe.planetracer.data.record.cluster.IncidenceMatrix;
import at.jwe.planetracer.data.record.highscore.Highscore;
import at.jwe.planetracer.data.record.level.LayerInfo;
import at.jwe.planetracer.data.record.level.Level;
import at.jwe.planetracer.data.record.level.Tileset;
import at.jwe.planetracer.repository.LevelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class DataServiceImpl implements DataService {

    private final LevelRepository levelRepository;

    @Override
    public Level addLevel(MapData mapData) {
        log.atInfo().log("Adding Map!");
        Optional<LevelEntity> existing = levelRepository.findByName(mapData.name());
        if (existing.isPresent()) {
            return getLevelOutput(existing.get());
        }

        List<Double> cellValues;

        try {
            cellValues = computeCellValues(mapData);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<Integer> resultList = normalizeAndCleanCellValues(cellValues);


        LevelEntity level = LevelEntity.builder()
                .initialTime(mapData.initialTime() != null ? mapData.initialTime() : 30)
                .name(mapData.name() != null ? mapData.name() : "NoName" + Math.random())
                .height(mapData.resolutionY())
                .width(mapData.resolutionX())
                .data(resultList.stream().mapToInt(i->i).toArray())
                .build();

        levelRepository.save(level);

        return getLevelOutput(level);
    }

    private static Level getLevelOutput(LevelEntity level) {
        return new Level(List.of(new LayerInfo(level.getName(), level.getData(), level.getHeight(), 1, "tilelayer", true, level.getWidth())),
                "orthogonal",
                List.of(new Tileset(1, "basicset", 16, 176, 0, "backgroundtileset", 0, 16, 16)));
    }

    private List<Double> computeCellValues(MapData mapData) throws InterruptedException {
        List<Double> cellValues;

        long floor = (long) Math.floor((double) mapData.resolutionY() / 4);

        ComputationRunnable run1 = new ComputationRunnable(mapData, 0, floor);
        ComputationRunnable run2 = new ComputationRunnable(mapData, floor, 2 * floor);
        ComputationRunnable run3 = new ComputationRunnable(mapData, 2 * floor, 3 * floor);
        ComputationRunnable run4 = new ComputationRunnable(mapData, 3 * floor, mapData.resolutionY());
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
    public ClusterResult getClusters(Long levelId) {

        return null;
    }

    @Override
    public List<IncidenceMatrix> getIncidences(Long levelId) {
        return null;
    }

    @Override
    public boolean addResult(PlayerResult playerResult) {
        return true;
    }

    @Override
    public Highscore getHighscore(Long levelId) {
        return null;
    }

    @Override
    public Level getLevelData(Long levelId) {
        Optional<LevelEntity> optionalLevel = levelRepository.findById(levelId);

        if (optionalLevel.isEmpty()) return null;
        LevelEntity level = optionalLevel.get();


        return getLevelOutput(level);
    }

    @Override
    public LevelOverview getLevelOverview() {
        List<LevelEntity> allLevels = levelRepository.findAll();
        if (allLevels.isEmpty()) {
            return null;
        }
        List<OverviewLevel> overviewList = new ArrayList<>();
        return new LevelOverview(overviewList);
    }
}
