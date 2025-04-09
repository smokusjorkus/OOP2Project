package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import main.Game;

public class LoadSave {

    public static final String PLAYER_ATLAS = "arthur_sprite.png";
    public static final String LEVEL_ATLAS = "outdoor_sprite.png";
    public static final String LEVEL_ONE_DATA = "sea.png";
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    
    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }
    
    public static int[][] GetLevelData(String fileName) {
        BufferedImage img = GetSpriteAtlas(fileName);
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];
        
        for(int j = 0; j < img.getHeight(); j++) {
            for(int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                if(value >= 111) {
                    value = 0; // Treat as transparent
                }
                lvlData[j][i] = value;
            }
        }
        return lvlData;
    }
}
