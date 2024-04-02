package at.jwe.snyder.data.record.level;

import at.jwe.snyder.data.record.LevelOverview;

public record LevelData(LevelOverview levelOverview, Level[] levels) {
}
