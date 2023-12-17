package at.jwe.planetracer.service;

import at.jwe.planetracer.data.entity.LevelEntity;
import at.jwe.planetracer.data.record.*;
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
    public boolean addLevel(MapData mapData) {
        log.atInfo().log("Adding Map!");
        Optional<LevelEntity> level = levelRepository.findByName(mapData.name());
        if (level.isPresent()) {
            return false;
        }

        List<Double> cellValues = new ArrayList<>();

        for (int x = 0; x < mapData.resolutionX(); x++) {
            for (int y = 0; y < mapData.resolutionY(); y++) {
                double cellValue = 0.0;
                Point currentPoint = new Point(x, y);
                for (DataPoint dataPoint : mapData.dataPoints()) {
                    // x -> exp(-beta*dist(x, x_0)
                    cellValue += Math.exp(-mapData.decay() * getDist(currentPoint, dataPoint));
                }
                cellValues.add(cellValue);
            }
        }

        OptionalDouble max = cellValues.stream().mapToDouble(v -> v).max();
        List<Double> resultList = new ArrayList<>();
        for (Double v : cellValues) {
            resultList.add(v / max.getAsDouble());
        }

        String resultingMapString = concatingResultMapString(resultList);


        LevelEntity levelEntity = LevelEntity.builder()
                .initialTime(mapData.initialTime() != null ? mapData.initialTime() : 30)
                .name(mapData.name() != null ? mapData.name() : "NoName" + Math.random())
                .height(mapData.resolutionY())
                .width(mapData.resolutionX())
                .data(resultingMapString)
                .build();

        levelRepository.save(levelEntity);
        return true;
    }

    private static String concatingResultMapString(List<Double> resultList) {
        log.atInfo().log("Creating Map String.");

        StringBuilder resultingMapString = new StringBuilder("[");

        for (int element = 0; element < resultList.size(); element++) {
            resultingMapString.append(element);

            if (element != (resultList.size() - 1)) {
                resultingMapString.append(", ");
            } else {
                resultingMapString.append("]");
            }
        }
        return resultingMapString.toString();
    }

    private Double getDist(Point point, DataPoint dataPoint) {
        return Math.sqrt(((dataPoint.y() - point.y()) * (dataPoint.y() - point.y())) + ((dataPoint.x() - point.x()) * (dataPoint.x() - point.x())));
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


        return new Level(List.of(new LayerInfo("World1", level.getData(), level.getHeight(), 1, "tilelayer", true, level.getWidth())),
                "orthogonal",
                List.of(new Tileset(1, "basicset", 16, 176, 0, "backgroundtileset", 0, 16, 16)));
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
