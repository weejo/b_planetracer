package at.jwe.snyder.data.record;

import at.jwe.snyder.data.record.cluster.ClusterCollection;
import at.jwe.snyder.data.record.highscore.HighscoreEntry;

public record Result(HighscoreEntry entry, ClusterCollection clusterCollection, int levelId) {
}
