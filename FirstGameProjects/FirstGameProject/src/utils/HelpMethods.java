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
		
		return isTileSolid((int)xIndex, (int) yIndex, lvlData);
	}
	
	public static boolean isTileSolid(int xTile, int yTile, int[][] lvlData) {
		int value = lvlData[yTile][xTile];
		
		if(value >= 111 || value < 0 || value == 46 || (value > 51 && value < 54))
			return true;
		return false;
	}
	
	public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
		return isSolid(hitbox.x + xSpeed, hitbox.y, lvlData);
	}
	
	public static boolean IsAllTileWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
		for(int i = 0; i < xEnd - xStart; i++)
			if(isTileSolid(xStart + i, y, lvlData))
				return false;
		return true;
	}
	
	public static boolean IsSightClear(int[][] lvlData,
            Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox) {
        int x0 = (int)((firstHitbox.x + firstHitbox.width / 2) / Game.TILES_SIZE);
        int y0 = (int)((firstHitbox.y + firstHitbox.height / 2) / Game.TILES_SIZE);
        int x1 = (int)((secondHitbox.x + secondHitbox.width / 2) / Game.TILES_SIZE);
        int y1 = (int)((secondHitbox.y + secondHitbox.height / 2) / Game.TILES_SIZE);
        
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        
        while (true) {
            if (isTileSolid(x0, y0, lvlData))
                return false;
            if (x0 == x1 && y0 == y1)
                break;
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
        return true;
    }
}
