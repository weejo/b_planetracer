package at.jwe.planetracer.service;

import at.jwe.planetracer.data.record.*;
import at.jwe.planetracer.data.record.cluster.ClusterResult;
import at.jwe.planetracer.data.record.highscore.Highscore;
import at.jwe.planetracer.data.record.level.Level;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface DataService {

    boolean addMap(MapData mapData) throws JsonProcessingException;

    ClusterResult getClusters(Long levelId) throws JsonProcessingException;

    Highscore getHighscore(Long levelId);

    boolean addResult(PlayerResult playerResult) throws JsonProcessingException;

    Level getLevelData(Long levelId) throws JsonProcessingException;

    LevelOverview getLevelOverview();
}
