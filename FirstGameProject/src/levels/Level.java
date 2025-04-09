package levels;

import java.util.List;

public class Level {
    
    private List<Level> layers;
    private int[][] lvlData;
    
    public Level(int[][] lvlData) {
        this.lvlData = lvlData;
    }
    
    public Level(List<Level> layers) {
        this.layers = layers;
        // Create a default empty level data if needed
        if (!layers.isEmpty()) {
            this.lvlData = new int[layers.get(0).getLevelData().length][layers.get(0).getLevelData()[0].length];
        }
    }
    
    public int getSpriteIndex(int x, int y) {
        // For composite level, return the topmost non-zero sprite
        if (layers != null && !layers.isEmpty()) {
            for (int i = layers.size() - 1; i >= 0; i--) {
                int sprite = layers.get(i).getSpriteIndex(x, y);
                if (sprite != 0) {
                    return sprite;
                }
            }
            return 0;
        }
        return lvlData[y][x];
    }
    
    public int[][] getLevelData() {
        return lvlData;
    }
    
    public List<Level> getLayers() {
        return layers;
    }
}