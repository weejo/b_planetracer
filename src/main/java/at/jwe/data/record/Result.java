package at.jwe.data.record;

import at.jwe.data.record.cluster.ClusterCollection;
import at.jwe.data.record.highscore.HighscoreEntry;

public record Result(HighscoreEntry entry, ClusterCollection clusterCollection, Long levelId) {
}
