package at.jwe.snyder.data.record;

import at.jwe.snyder.data.record.highscore.HighscoreEntry;

public record PlayerResult(HighscoreEntry entry, Long levelId, PlayerData playerData) {
}
