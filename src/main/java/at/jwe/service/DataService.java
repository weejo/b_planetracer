package at.jwe.service;

import at.jwe.data.entity.ResultEntity;
import at.jwe.data.record.highscore.Highscore;
import at.jwe.data.record.*;

import java.util.List;

public interface DataService {

    boolean addMap(MapData mapData);

    List<ResultEntity> getClusters(Long levelId);

    Highscore getHighscore(Long levelId);

    Highscore addResult(Result result);

    LevelData getLevelData();

    LevelOverview getLevelOverview();
}
