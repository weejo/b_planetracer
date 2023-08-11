package at.jwe.planetracer.service;

import at.jwe.planetracer.data.entity.HighscoreEntity;
import at.jwe.planetracer.data.entity.LevelEntity;
import at.jwe.planetracer.data.entity.ResultEntity;
import at.jwe.planetracer.data.record.*;
import at.jwe.planetracer.data.record.cluster.Cluster;
import at.jwe.planetracer.data.record.highscore.Highscore;
import at.jwe.planetracer.data.record.highscore.HighscoreEntry;
import at.jwe.planetracer.repository.HighscoreRepository;
import at.jwe.planetracer.repository.LevelRepository;
import at.jwe.planetracer.repository.ResultRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional
public class DataServiceImpl implements DataService {

    private final HighscoreRepository highscoreRepository;
    private final LevelRepository levelRepository;
    private final ResultRepository resultRepository;
    private final ObjectMapper objectMapper;

    @Override
    public boolean addMap(MapData mapData) throws JsonProcessingException {
        Optional<LevelEntity> level = levelRepository.findByName(mapData.name());
        if (level.isPresent()) {
            return false;
        }

        Long mostLeft = 0L;
        Long mostRight = 0L;
        Long mostUp = 0L;
        Long mostDown = 0L;

        Random random = new Random();
        List<EnrichedPlanet> enrichedPlanets = new ArrayList<>();

        for (Planet planet : mapData.planets()) {
            if (planet.x() > mostRight) {
                mostRight = planet.x();
            }
            if (planet.x() < mostLeft) {
                mostLeft = planet.x();
            }
            if (planet.y() > mostUp) {
                mostUp = planet.y();
            }
            if (planet.y() < mostDown) {
                mostDown = planet.y();
            }
            enrichedPlanets.add(new EnrichedPlanet(planet.x(), planet.y(), random.nextInt(6), "planet"));
        }
        String objects = objectMapper.writeValueAsString(enrichedPlanets);

        LevelEntity levelEntity = LevelEntity.builder()
                .initialTime(mapData.initialTime())
                .maxDistance(mapData.maxDistance())
                .name(mapData.name())
                .height(Math.abs(mostUp) + Math.abs(mostDown) + 200)
                .width(Math.abs(mostLeft) + Math.abs(mostRight) + 200)
                .levelX(mostLeft + 100)
                .levelY(mostUp + 100)
                .objects(objects)
                .build();

        levelRepository.save(levelEntity);
        return true;
    }


    @Override
    public List<ResultEntity> getClusters(Long levelId) {
        return resultRepository.findAllByLevelId(levelId);
    }

    @Override
    public Highscore getHighscore(Long levelId) {
        Optional<LevelEntity> level = levelRepository.findById(levelId);
        if (level.isPresent()) {
            List<HighscoreEntity> highscoreEntities = highscoreRepository.findAllByLevelId(levelId);

            List<HighscoreEntry> highscoreEntries = new ArrayList<>();

            for (HighscoreEntity highscoreEntity : highscoreEntities) {
                highscoreEntries.add(new HighscoreEntry(highscoreEntity.getPoints(), highscoreEntity.getName()));
            }
            return new Highscore(highscoreEntries);
        } else {
            return null;
        }
    }

    @Override
    public Highscore addResult(Result result) throws JsonProcessingException {
        Long levelId = result.levelId();

        List<Cluster> clusters = result.clusterCollection().clusters();
        String clusterJSON = objectMapper.writeValueAsString(clusters);

        ResultEntity resultEntity = ResultEntity.builder()
                .result(clusterJSON)
                .levelId(levelId)
                .build();

        resultRepository.save(resultEntity);

        List<HighscoreEntity> allByLevelId = highscoreRepository.findAllByLevelId(levelId);
        Collections.sort(allByLevelId, Comparator.comparingInt(HighscoreEntity::getPoints));

        HighscoreEntity newScore = HighscoreEntity.builder()
                .points(result.entry().points())
                .name(result.entry().name())
                .levelId(levelId)
                .build();

        if (allByLevelId.size() <= 5) {
            allByLevelId.add(newScore);
            highscoreRepository.save(newScore);
        } else {
            HighscoreEntity last = allByLevelId.get(allByLevelId.size() - 1);
            if (result.entry().points() > last.getPoints()) {
                allByLevelId.remove(last);
                highscoreRepository.save(newScore);
                allByLevelId.add(newScore);
            }
        }
        List<HighscoreEntry> highscore = new ArrayList<>();
        for (HighscoreEntity highscoreEntity : allByLevelId) {
            highscore.add(new HighscoreEntry(highscoreEntity.getPoints(), highscoreEntity.getName()));
        }
        return new Highscore(highscore);
    }

    @Override
    public LevelData getLevelData() {
        List<LevelEntity> allLevels = levelRepository.findAll();
        for (LevelEntity allLevel : allLevels) {

        }
        return null;
    }

    @Override
    public LevelOverview getLevelOverview() {
        List<LevelEntity> allLevels = levelRepository.findAll();
        if (allLevels.isEmpty()) {
            return null;
        }
        List<OverviewLevels> overviewList = new ArrayList<>();
        for (LevelEntity level : allLevels) {
            overviewList.add(new OverviewLevels(level.getName(), level.getWidth().toString() + "x" + level.getHeight().toString()));
        }
        return new LevelOverview(overviewList);
    }
}
