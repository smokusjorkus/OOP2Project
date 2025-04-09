package utils;

import main.Game;

public class Constants {
	
	public static class UI{
		public static class Buttons{
			
			public static final int B_WIDTH_DEFAULT = 140;
			public static final int B_HEIGHT_DEFAULT = 56;
			public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
			public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
		}
	}
	
	public static class EnemyConstants{
		public static final int SLIME = 0;
		
		public static final int IDLE = 0;
		public static final int RUNNING_DOWN = 4;
		public static final int RUNNING_UP = 5;
		public static final int RUNNING_LEFT = 6;
		public static final int RUNNING_RIGHT = 7;
		public static final int ATTACKING = 8;
		public static final int HURT = 12;
		public static final int DEATH = 16;
		
		public static final int SLIME_WIDTH_DEFAULT = 64;
		public static final int SLIME_HEIGHT_DEFAULT = 64;
		
		public static final int SLIME_WIDTH = (int) (SLIME_WIDTH_DEFAULT * Game.SCALE);
		public static final int SLIME_HEIGHT = (int) (SLIME_HEIGHT_DEFAULT * Game.SCALE);
		
		public static final int SLIME_DRAWOFFSET_X = (int) (20 * Game.SCALE);
		public static final int SLIME_DRAWOFFSET_Y = (int) (20 * Game.SCALE);

		public static int GetSpriteAmount(int enemy_type, int enemy_state) {
			
			switch(enemy_type) {
			case SLIME:
				switch(enemy_state) {
				case IDLE:
					return 6;
				case RUNNING_DOWN:
				case RUNNING_UP:
				case RUNNING_LEFT:
				case RUNNING_RIGHT:
					return 8;
				case ATTACKING:
					return 10;
				case HURT:
					return 5;
				case DEATH:
					return 10;
				}
				
			}
			
			return 0;
		}
	}
	
	public static class Directions{
		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;
	}
	
	public static class PlayerConstants{
		public static final int WALKING_UP = 0;
		public static final int WALKING_LEFT = 1;
		public static final int WALKING_DOWN = 2;
		public static final int WALKING_RIGHT = 3;
		public static final int DYING = 4;
		public static final int ATTACK_UP = 5;
		public static final int ATTACK_LEFT = 6;
		public static final int ATTACK_DOWN = 7;
		public static final int ATTACK_RIGHT = 8;
		
		public static int GetSpriteAmount(int player_action) {
			
			switch(player_action) {
			case WALKING_DOWN:
			case WALKING_UP:
			case WALKING_LEFT:
			case WALKING_RIGHT:
				return 8;
			case DYING:
			case ATTACK_DOWN:
			case ATTACK_UP:
			case ATTACK_RIGHT:
			case ATTACK_LEFT:
				return 6;
			default:
				return 1;
			}
			
		}
	}
}
