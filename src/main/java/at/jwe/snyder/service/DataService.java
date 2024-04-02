package at.jwe.snyder.service;

import at.jwe.snyder.data.record.LevelOverview;
import at.jwe.snyder.data.record.data.MapData;
import at.jwe.snyder.data.record.PlayerResult;
import at.jwe.snyder.data.record.cluster.ClusterResult;
import at.jwe.snyder.data.record.highscore.Highscore;
import at.jwe.snyder.data.record.level.Level;

public interface DataService {

    Level addLevel(MapData mapData);

    ClusterResult getClusters(Long levelId, Long threshold);

    Highscore getHighscore(Long levelId);

    void addResult(PlayerResult result);

    Level getLevelData(Long levelId);

    LevelOverview getLevelOverview();

}