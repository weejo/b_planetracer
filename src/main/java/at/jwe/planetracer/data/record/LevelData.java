package at.jwe.planetracer.data.record;


import at.jwe.planetracer.data.record.level.Level;

import java.util.List;

public record LevelData(LevelOverview levelOverview, List<Level> levels) {
}
