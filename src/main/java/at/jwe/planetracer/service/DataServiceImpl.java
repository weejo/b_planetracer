package at.jwe.planetracer.service;

import at.jwe.planetracer.data.entity.HighscoreEntity;
import at.jwe.planetracer.data.entity.LevelEntity;
import at.jwe.planetracer.data.entity.ResultEntity;
import at.jwe.planetracer.data.record.*;
import at.jwe.planetracer.data.record.cluster.Cluster;
import at.jwe.planetracer.data.record.cluster.ClusterCollection;
import at.jwe.planetracer.data.record.cluster.ClusterResult;
import at.jwe.planetracer.data.record.highscore.Highscore;
import at.jwe.planetracer.data.record.highscore.HighscoreEntry;
import at.jwe.planetracer.data.record.level.LayerInfo;
import at.jwe.planetracer.data.record.level.Level;
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

        Long mostLeft = null;
        Long mostRight = null;
        Long mostUp = null;
        Long mostDown = null;

        Random random = new Random();
        List<EnrichedPlanet> enrichedPlanets = new ArrayList<>();

        for (Planet planet : mapData.planets()) {
            if (mostRight == null || planet.x() > mostRight) {
                mostRight = planet.x();
            }
            if (mostLeft == null || planet.x() < mostLeft) {
                mostLeft = planet.x();
            }
            if (mostUp == null || planet.y() > mostUp) {
                mostUp = planet.y();
            }
            if (mostDown == null || planet.y() < mostDown) {
                mostDown = planet.y();
            }
            enrichedPlanets.add(new EnrichedPlanet(planet.x(), planet.y(), random.nextInt(6), "planet"));
        }

        if (mostUp == null || mostRight == null || mostLeft == null || mostDown == null) return false;


        String objects = objectMapper.writeValueAsString(enrichedPlanets);

        LevelEntity levelEntity = LevelEntity.builder()
                .initialTime(mapData.initialTime())
                .maxDistance(mapData.maxDistance())
                .name(mapData.name())
                .height(Math.abs(mostUp) + Math.abs(mostDown) + 600)
                .width(Math.abs(mostLeft) + Math.abs(mostRight) + 600)
                .levelX(mostLeft - 500)
                .levelY(mostUp - 500)
                .objects(objects)
                .build();

        levelRepository.save(levelEntity);
        return true;
    }


    @Override
    public ClusterResult getClusters(Long levelId) throws JsonProcessingException {

        List<ResultEntity> allByLevelId = resultRepository.findAllByLevelId(levelId);
        List<ClusterCollection> clusterCollections = new ArrayList<>();
        for (ResultEntity resultEntity : allByLevelId) {

            List<Cluster> clusterList = objectMapper.readValue(resultEntity.getResult(), objectMapper.getTypeFactory().constructCollectionType(List.class, Cluster.class));

            clusterCollections.add(new ClusterCollection(clusterList));
        }
        return new ClusterResult(clusterCollections);
    }

    @Override
    public boolean addResult(PlayerResult playerResult) throws JsonProcessingException {
        Long levelId = playerResult.levelId();

        // We only want winners - otherwise it would not be fully clustered data.
        List<Cluster> clusters = playerResult.clusters();
        if (!clusters.isEmpty()) {

            String clusterJSON = objectMapper.writeValueAsString(clusters);

            ResultEntity resultEntity = ResultEntity.builder()
                    .result(clusterJSON)
                    .levelId(levelId)
                    .build();

            resultRepository.save(resultEntity);
        }

        List<HighscoreEntity> allByLevelId = highscoreRepository.findAllByLevelId(levelId);

        HighscoreEntity newScore = HighscoreEntity.builder()
                .points(playerResult.entry().points())
                .name(playerResult.entry().name())
                .levelId(levelId)
                .build();

        if (allByLevelId.size() < 5) {
            allByLevelId.add(newScore);
            highscoreRepository.save(newScore);
            return true;
        } else {
            HighscoreEntity lowest = null;
            for (HighscoreEntity highscoreEntity : allByLevelId) {
                if (lowest == null || lowest.getPoints() > highscoreEntity.getPoints() && highscoreEntity.getPoints() < newScore.getPoints()) {
                    lowest = highscoreEntity;
                }
            }
            if (lowest != null) {
                highscoreRepository.delete(lowest);
                highscoreRepository.save(newScore);
                allByLevelId.remove(lowest);
                allByLevelId.add(newScore);
                return true;
            }
        }
        return false;
    }

    @Override
    public Highscore getHighscore(Long levelId) {
        Optional<LevelEntity> level = levelRepository.findById(levelId);

        if (level.isEmpty()) return null;

        List<HighscoreEntity> highscoreEntities = highscoreRepository.findAllByLevelId(levelId);

        List<HighscoreEntity> sorted = highscoreEntities.stream()
                .sorted(Comparator.comparing(HighscoreEntity::getPoints).reversed())
                .toList();

        List<HighscoreEntry> highscore = new ArrayList<>();
        for (HighscoreEntity highscoreEntity : sorted) {
            highscore.add(new HighscoreEntry(highscoreEntity.getPoints(), highscoreEntity.getName()));
        }

        return new Highscore(highscore);
    }

    @Override
    public Level getLevelData(Long levelId) throws JsonProcessingException {
        Optional<LevelEntity> optionalLevel = levelRepository.findById(levelId);

        if (optionalLevel.isEmpty()) return null;
        LevelEntity level = optionalLevel.get();

        List<EnrichedPlanet> enrichedPlanets = objectMapper.readValue(level.getObjects(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, EnrichedPlanet.class));

        return new Level(Arrays.asList(new LayerInfo("PLANET_LAYER", enrichedPlanets, "objectgroup")),
                "orthogonal", new ArrayList<>());
    }

    @Override
    public LevelOverview getLevelOverview() {
        List<LevelEntity> allLevels = levelRepository.findAll();
        if (allLevels.isEmpty()) {
            return null;
        }
        List<OverviewLevel> overviewList = new ArrayList<>();
        for (LevelEntity level : allLevels) {
            overviewList.add(new OverviewLevel(level.getName(), level.getWidth().toString() + "x" + level.getHeight().toString(), level.getLevelId(), level.getMaxDistance(), level.getInitialTime(),
                    level.getLevelX(), level.getLevelY(), level.getWidth(), level.getHeight()));
        }
        return new LevelOverview(overviewList);
    }
}
