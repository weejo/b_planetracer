package at.jwe.planetracer.data.record;

import at.jwe.planetracer.data.record.highscore.HighscoreEntry;

import java.util.List;

public record PlayerResult(HighscoreEntry entry, List<List<Planet>> clusterList, Long levelId) {
}
