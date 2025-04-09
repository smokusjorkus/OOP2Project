package utils;

import java.awt.geom.Rectangle2D;

import main.Game;

public class HelpMethods {
	
	public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
		
		if(!isSolid(x,y,lvlData))
			if(!isSolid(x+width, y+height, lvlData))
				if(!isSolid(x+width, y, lvlData))
					if(!isSolid(x, y+height, lvlData))
						return true;
		return false;
	}
	
	private static boolean isSolid(float x, float y, int[][] lvlData) {
		int maxWidth = lvlData[0].length * Game.TILES_SIZE;
		int maxHeight = lvlData.length * Game.TILES_SIZE;
		if (x < 0 || x >= maxWidth)
			return true;
		if (y < 0 || y >= maxHeight)
			return true;
		
		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;
		
		int value = lvlData[(int) yIndex][(int) xIndex];
		
		if(value >= 111 || value < 0 || value == 46 || (value > 51 && value < 54))
			return true;
		return false;
	}
	
	public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
		return isSolid(hitbox.x + xSpeed, hitbox.y, lvlData);
	}
}
