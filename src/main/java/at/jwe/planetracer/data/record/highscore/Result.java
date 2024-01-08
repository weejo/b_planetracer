package at.jwe.planetracer.data.record.highscore;

import at.jwe.planetracer.data.record.cluster.ClusterCollection;

public record Result(HighscoreEntry entry, ClusterCollection clusterCollection, Long levelId) {
}
