package at.jwe.planetracer.data.record;

import at.jwe.planetracer.data.record.cluster.ClusterCollection;
import at.jwe.planetracer.data.record.highscore.HighscoreEntry;

public record Result(HighscoreEntry entry, ClusterCollection clusterCollection, Long levelId) {
}
