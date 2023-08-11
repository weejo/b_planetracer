package at.jwe.planetracer.data.record.level;

import java.util.List;

public record Level(LayerInfo layers, String orientation, List<String> tilesets, Long maxDistance, Long initialTime,
                    Long level_x, Long level_y, Long width, Long height) {
}
