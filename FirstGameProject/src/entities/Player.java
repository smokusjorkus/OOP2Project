package entities;

import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.CanMoveHere;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import main.Game;

import utils.LoadSave;

public class Player extends Entity{

	private BufferedImage[][] animations;
	private int aniTick, aniIndex, aniSpeed = 16;
	private int playerAction;
	private boolean attacking = false;
	private boolean left, up, right, down;
	private float playerSpeed = 1.0f * Game.SCALE;
	private int facingDirection = WALKING_DOWN;
	private int[][] lvlData;
	private float xDrawOffset = 80 * Game.SCALE;
	private float yDrawOffset = 78 * Game.SCALE;
	
	
	public Player(float x, float y, int width, int height) {
		super(x, y, width, height);
		loadAnimations();
		initHitbox(x, y, 32 * Game.SCALE, 48 * Game.SCALE);
	}
	
	public void update() {
	    if (attacking) {
	        updateAnimationTick();
	    } else if (left || up || down || right) {
	        updatePos();
	        updateAnimationTick();
	        setAnimation();
	    }
	}

	
	public void render(Graphics g, int xLvlOffset, int yLvlOffset) {
		g.drawImage(animations[playerAction][aniIndex], (int)(hitbox.x - xDrawOffset) - xLvlOffset, 
				(int)(hitbox.y - yDrawOffset) - yLvlOffset, width, height, null);
//		drawHitbox(g, xLvlOffset, yLvlOffset);
	}

	private void updateAnimationTick() {
		aniTick++;
	    if (aniTick >= aniSpeed) {
	        aniTick = 0;
	        aniIndex++;
	        if (aniIndex >= GetSpriteAmount(playerAction)) {
	            aniIndex = 0;
	            attacking = false;
	        }
	    }
	}
	
	private void setAnimation() {
	    int startAni = playerAction;

	    if (left && !right) {
	        playerAction = WALKING_LEFT;
	        facingDirection = WALKING_LEFT;
	    } else if (right && !left) {
	        playerAction = WALKING_RIGHT;
	        facingDirection = WALKING_RIGHT;
	    }

	    if (up && !down) {
	        playerAction = WALKING_UP;
	        facingDirection = WALKING_UP;
	    } else if (down && !up) {
	        playerAction = WALKING_DOWN;
	        facingDirection = WALKING_DOWN;
	    }

	    if (startAni != playerAction) {
	        resetAniTick();
	    }
	}
	
	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
		
	}
	
	private void updatePos() {
		if(!left && !right && !up && !down)
			return;
		
		float xSpeed = 0, ySpeed = 0;
		
		if(left && !right) 
			xSpeed -= playerSpeed;
		else if (right && !left) 
			xSpeed += playerSpeed;
	
		if(up && !down) 
			ySpeed -= playerSpeed;
		else if (down && !up)
			ySpeed += playerSpeed;
		
//		if(CanMoveHere(x+xSpeed, y+ySpeed, width, height, lvlData)) {
//			this.x += xSpeed;
//			this.y += ySpeed;
//		}
		
		if(CanMoveHere(hitbox.x+xSpeed, hitbox.y+ySpeed, hitbox.width, hitbox.height, lvlData)) {
			hitbox.x += xSpeed;
			hitbox.y += ySpeed;
		}
		
	}
	
	private void loadAnimations() {
		
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
			
		animations = new BufferedImage[9][8];
		for(int j = 0; j < animations.length; j++)
			for(int i = 0; i < animations[j].length; i++)
				animations[j][i] = img.getSubimage(i*192, j*192, 192, 192);
	}
	
	public void loadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
	}
	
	public void resetDirBooleans() {
		left = false;
		right = false;
		up = false;
		down = false;
	}
	
	public void setAttacking(boolean attacking) {
	    this.attacking = attacking;
	    if (attacking) {
	        switch (facingDirection) {
	            case WALKING_LEFT:
	                playerAction = ATTACK_LEFT;
	                break;
	            case WALKING_RIGHT:
	                playerAction = ATTACK_RIGHT;
	                break;
	            case WALKING_UP:
	                playerAction = ATTACK_UP;
	                break;
	            case WALKING_DOWN:
	                playerAction = ATTACK_DOWN;
	                break;
	        }
	        resetAniTick();
	    }
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}
}
