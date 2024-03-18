package at.jwe.planetracer.data.record;

import at.jwe.planetracer.data.record.highscore.HighscoreEntry;

public record PlayerResult(HighscoreEntry entry, Long levelId, PlayerData playerData) {
}
