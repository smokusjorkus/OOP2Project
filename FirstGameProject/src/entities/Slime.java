package entities;

import static utils.Constants.EnemyConstants.*;

import main.Game;
public class Slime extends Enemy{

	public Slime(float x, float y) {
		super(x, y, SLIME_WIDTH, SLIME_HEIGHT, SLIME);
		initHitbox(x, y, (int) (22 * Game.SCALE),(int) (20 * Game.SCALE));
		
	}
	
}
