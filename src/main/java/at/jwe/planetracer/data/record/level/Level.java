package at.jwe.planetracer.data.record.level;

import java.lang.reflect.Array;

public record Level(LayerInfo layers, String orientation, Array tilesets, Long maxDistance, Long initialTime, Long level_x, Long level_y, Long width, Long height) {
}
