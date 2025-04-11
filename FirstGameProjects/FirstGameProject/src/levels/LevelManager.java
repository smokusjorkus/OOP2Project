package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import main.Game;
import utils.LoadSave;

public class LevelManager {

    private Game game;
    private BufferedImage[] levelSprite;
    private Level levelOne;

    public LevelManager(Game game) {
        this.game = game;
        importOutsideSprites();
        createLayeredLevel();
    }
    
    private void createLayeredLevel() {
        List<Level> layers = new ArrayList<>();
        // Load each layer in order (bottom to top)
        layers.add(new Level(LoadSave.GetLevelData("levelOne_layer1.png")));
        layers.add(new Level(LoadSave.GetLevelData("levelOne_layer2.png")));
    
        // Create a composite level with all layers
        levelOne = new Level(layers);
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[111];
        for (int j = 0; j < 37; j++) {
            for (int i = 0; i < 3; i++) {
                int index = j * 3 + i;
                levelSprite[index] = img.getSubimage(i * 32, j * 32, 32, 32);
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
        // Draw each layer in order
        for (Level layer : levelOne.getLayers()) {
            for (int j = 0; j < layer.getLevelData().length; j++) {
                for (int i = 0; i < layer.getLevelData()[0].length; i++) {
                    int index = layer.getSpriteIndex(i, j);
                    if (index != 0) { // Only draw if not transparent
                        g.drawImage(levelSprite[index], 
                            Game.TILES_SIZE * i - xLvlOffset, 
                            Game.TILES_SIZE * j - yLvlOffset, 
                            Game.TILES_SIZE, Game.TILES_SIZE, null);
                    }
                }
            }
        }
    }

    public void update() {
        // Future updates can be added here
    }
    
    public Level getCurrentLevel() {
    	return levelOne;
    }
}
