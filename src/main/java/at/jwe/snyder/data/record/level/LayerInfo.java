package at.jwe.snyder.data.record.level;

public record LayerInfo(String name, int[] data, Long height, double opacity, String type, boolean visible, Long width, int x, int y) {
}
