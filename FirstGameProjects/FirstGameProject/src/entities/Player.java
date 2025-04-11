package entities;

import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.CanMoveHere;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gamestates.Playing;
import main.Game;

import utils.LoadSave;

public class Player extends Entity{

	private BufferedImage[][] animations;
	private int aniTick, aniIndex, aniSpeed = 16;
	private int playerAction;
	private boolean attacking = false;
	private boolean left, up, right, down;
	private float playerSpeed = 0.7f * Game.SCALE;
	private int facingDirection = WALKING_DOWN;
	private int[][] lvlData;
	private float xDrawOffset = 80 * Game.SCALE;
	private float yDrawOffset = 78 * Game.SCALE;
	
	//StatusBarUi
	private BufferedImage statusBarImg;
	
	private int statusBarWidth = (int) (192 * Game.SCALE);
	private int statusBarHeight = (int) (58 * Game.SCALE);
	private int statusBarX = (int) (10 * Game.SCALE);
	private int statusBarY = (int) (10 * Game.SCALE);
	
	private int healthBarWidth = (int) (150 * Game.SCALE);
	private int healthBarHeight = (int) (4 * Game.SCALE);
	private int healthBarXStart = (int) (34 * Game.SCALE);
	private int healthBarYStart = (int) (14 * Game.SCALE);
	
	private int maxHealth = 100;
	private int currentHealth = maxHealth;
	private int healthWidth = healthBarWidth;
	
	//AttackBox
	private Rectangle2D.Float attackBox;
	
	private boolean attackChecked;
	private Playing playing;
	public Player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);
		this.playing = playing;
		loadAnimations();
		initHitbox(x, y, 32 * Game.SCALE, 48 * Game.SCALE);
		initAttackBox();
	}
	
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x,y, (int) (30 * Game.SCALE), (int) (30 * Game.SCALE));
		
	}

	public void update() {
		updateHealthBar();
		
		if(currentHealth <= 0) {
			playing.setGameOver(true);
			return;
		}
		
		updateAttackBox();
		
	    if (attacking) {
	    	checkAttack();
	        updateAnimationTick();
	    } else if (left || up || down || right) {
	        updatePos();
	        updateAnimationTick();
	        setAnimation();
	    }
	}

	
	private void checkAttack() {
		if(attackChecked || aniIndex <= 4)
			return;
		attackChecked = true;
		playing.checkEnemyHit(attackBox);
	}

	private void updateAttackBox() {
	    float xOffset = Game.SCALE * 10;
	    float yOffset = Game.SCALE * -3; // smaller Y offset to keep it closer

	    if (right) {
	        attackBox.x = hitbox.x + hitbox.width + xOffset;
	        attackBox.y = hitbox.y + (hitbox.height / 2) - (attackBox.height / 2) + yOffset;
	    } else if (left) {
	        attackBox.x = hitbox.x - attackBox.width - xOffset;
	        attackBox.y = hitbox.y + (hitbox.height / 2) - (attackBox.height / 2) + yOffset;
	    } else if (up) {
	        attackBox.x = hitbox.x + (hitbox.width / 2) - (attackBox.width / 2);
	        attackBox.y = hitbox.y - attackBox.height - yOffset;
	    } else if (down) {
	        attackBox.x = hitbox.x + (hitbox.width / 2) - (attackBox.width / 2);
	        attackBox.y = hitbox.y + hitbox.height + yOffset;
	    }
	}



	private void updateHealthBar() {
		healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
		
	}
	
	public void changeHealth(int value) {
		currentHealth += value;
		
		if(currentHealth <= 0) {
			currentHealth = 0;
//			gameOver();
		}else if(currentHealth >= maxHealth)
			currentHealth = maxHealth;
		
	}

	public void render(Graphics g, int xLvlOffset, int yLvlOffset) {
		g.drawImage(animations[playerAction][aniIndex], (int)(hitbox.x - xDrawOffset) - xLvlOffset, 
				(int)(hitbox.y - yDrawOffset) - yLvlOffset, width, height, null);
//		drawHitbox(g, xLvlOffset, yLvlOffset);
		drawAttackBox(g, xLvlOffset, yLvlOffset);
	}

	private void drawAttackBox(Graphics g, int xLvlOffset, int yLvlOffset) {
		g.setColor(Color.red);
		g.drawRect((int)attackBox.x - xLvlOffset, (int)attackBox.y - yLvlOffset, (int)attackBox.width, (int)attackBox.height);
	}

	public void drawUI(Graphics g) {
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		g.setColor(Color.red);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
	}

	private void updateAnimationTick() {
		aniTick++;
	    if (aniTick >= aniSpeed) {
	        aniTick = 0;
	        aniIndex++;
	        if (aniIndex >= GetSpriteAmount(playerAction)) {
	            aniIndex = 0;
	            attacking = false;
	            attackChecked = false;
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
		
		statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
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

	public void resetAll() {
		resetDirBooleans();
		attacking = false;
		currentHealth = maxHealth;
		
		hitbox.x = x;
		hitbox.y = y;
	}
}
