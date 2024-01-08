package at.jwe.planetracer.data.record.level;

import at.jwe.planetracer.data.record.LevelOverview;

public record LevelData(LevelOverview levelOverview, Level[] levels) {
}
