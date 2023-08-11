package at.jwe.planetracer.data.record;

import at.jwe.planetracer.data.record.level.Level;

public record LevelData(LevelOverview levelOverview, Level[] levels) {
}
