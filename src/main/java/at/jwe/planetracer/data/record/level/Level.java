package at.jwe.planetracer.data.record.level;

import java.util.List;

public record Level(List<LayerInfo> layers, String orientation, int tilewidth, int tileheight, List<Tileset> tilesets) {
}
