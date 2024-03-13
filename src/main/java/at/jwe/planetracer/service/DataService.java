package at.jwe.planetracer.service;

import at.jwe.planetracer.data.record.LevelOverview;
import at.jwe.planetracer.data.record.data.MapData;
import at.jwe.planetracer.data.record.data.SurveyData;
import at.jwe.planetracer.data.record.highscore.PlayerResult;
import at.jwe.planetracer.data.record.cluster.ClusterResult;
import at.jwe.planetracer.data.record.highscore.Highscore;
import at.jwe.planetracer.data.record.level.Level;

public interface DataService {

    Level addLevel(MapData mapData);

    ClusterResult getClusters(Long levelId, Long threshold);

    Highscore getHighscore(Long levelId);

    void addResult(PlayerResult result);

    Level getLevelData(Long levelId);

    LevelOverview getLevelOverview();

    void addSurvey(Long surveyId, SurveyData surveyData);
}
