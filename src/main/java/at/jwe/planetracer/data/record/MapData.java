package at.jwe.planetracer.data.record;

public record MapData(String name, Long maxDistance, Long initialTime, Planet[] planets) {
}