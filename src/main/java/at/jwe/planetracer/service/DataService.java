package at.jwe.planetracer.service;

import at.jwe.planetracer.data.record.*;
import at.jwe.planetracer.data.record.highscore.Highscore;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface DataService {

    boolean addMap(MapData mapData) throws JsonProcessingException;

    ClusterResult getClusters(Long levelId) throws JsonProcessingException;

    Highscore getHighscore(Long levelId);

    Highscore addResult(PlayerResult playerResult) throws JsonProcessingException;

    LevelData getLevelData() throws JsonProcessingException;

    LevelOverview getLevelOverview();
}
