package at.jwe.planetracer.service;

import at.jwe.planetracer.data.entity.ResultEntity;
import at.jwe.planetracer.data.record.LevelData;
import at.jwe.planetracer.data.record.LevelOverview;
import at.jwe.planetracer.data.record.MapData;
import at.jwe.planetracer.data.record.Result;
import at.jwe.planetracer.data.record.highscore.Highscore;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface DataService {

    boolean addMap(MapData mapData) throws JsonProcessingException;

    List<ResultEntity> getClusters(Long levelId);

    Highscore getHighscore(Long levelId);

    Highscore addResult(Result result) throws JsonProcessingException;

    LevelData getLevelData();

    LevelOverview getLevelOverview();
}
