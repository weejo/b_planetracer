package at.jwe.snyder.data.record;

import at.jwe.snyder.data.record.highscore.HighscoreEntry;

public record PlayerResult(HighscoreEntry entry, int levelId, PlayerData playerData) {
}
