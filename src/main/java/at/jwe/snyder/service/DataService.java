package at.jwe.snyder.service;

import at.jwe.snyder.data.record.LevelOverview;
import at.jwe.snyder.data.record.PlayerResult;
import at.jwe.snyder.data.record.data.MapData;
import at.jwe.snyder.data.record.highscore.Highscore;
import at.jwe.snyder.data.record.level.Level;
import at.jwe.snyder.data.record.output.Solution;

import java.util.List;

public interface DataService {

    Level addLevel(MapData mapData);

    Solution computeSolution(int levelId, float cutoff);

    Highscore getHighscore(int levelId);

    void addResult(PlayerResult result);

    Level getLevelData(int levelId);

    LevelOverview getLevelOverview();

    List<Solution> getAllSolutions(int levelId);
}
