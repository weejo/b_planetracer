package at.jwe.snyder.data.record.level;

import java.util.List;

public record Level(List<LayerInfo> layers, String orientation, int tilewidth, int tileheight, Long height, Long width, List<Tileset> tilesets) {
}