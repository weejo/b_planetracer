package at.jwe.snyder.data.record.level;

import java.util.List;

public record Level(List<LayerInfo> layers, String orientation, int tilewidth, int tileheight, int height, int width, List<Tileset> tilesets) {
}
