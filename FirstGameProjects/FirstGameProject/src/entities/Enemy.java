package entities;

import static utils.Constants.EnemyConstants.*;
import static utils.HelpMethods.*;

import main.Game;

import static utils.Constants.Directions.*;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.util.Random;

public abstract class Enemy extends Entity {
    protected int aniIndex, enemyState, enemyType;
    protected int aniTick, aniSpeed = 25;
    protected float walkSpeed = 0.2f * Game.SCALE;
    protected boolean isAttacking = false;
    // Maximum distance the enemy will travel before changing to a random direction.
    protected float maxPatrolDistance = 100 * Game.SCALE;
    // Accumulates the distance traveled during the current patrol.
    protected float currentPatrolDistance = 0;
    protected Random rand = new Random();
    protected float attackDistance = Game.TILES_SIZE;
    
    protected int maxHealth;
    protected int currentHealth;
    protected boolean active = true;
    protected boolean attackChecked;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitbox(x, y, width, height);
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
        chooseRandomDirection();;
    }
    
    protected boolean canSeePlayer(int[][] lvlData, Player player) {
    	if(isPlayerInRange(player)) {
    		if(IsSightClear(lvlData, hitbox ,player.hitbox))
    			return true;
    	}
    	return false;
    }
    
    protected void turnTowardsPlayer(Player player) {
        float enemyCenterX = hitbox.x + hitbox.width / 2;
        float enemyCenterY = hitbox.y + hitbox.height / 2;
        float playerCenterX = player.hitbox.x + player.hitbox.width / 2;
        float playerCenterY = player.hitbox.y + player.hitbox.height / 2;

        float dx = playerCenterX - enemyCenterX;
        float dy = playerCenterY - enemyCenterY;

        float threshold = 15 * Game.SCALE; // adjust as needed for smoothness
        
        if (Math.abs(dx) - Math.abs(dy) > threshold) {
            if (dx > 0) {
                newState(RUNNING_RIGHT);
            } else {
                newState(RUNNING_LEFT);
            }
        } else if (Math.abs(dy) - Math.abs(dx) > threshold) {
            if (dy > 0) {
                newState(RUNNING_DOWN);
            } else {
                newState(RUNNING_UP);
            }
        }
        // If the difference is too small, keep the current direction — no update.
    }

    
    protected boolean isPlayerInRange(Player player) {
        // Calculate centers of the enemy and player's hitboxes
        float enemyCenterX = hitbox.x + hitbox.width / 2;
        float enemyCenterY = hitbox.y + hitbox.height / 2;
        float playerCenterX = player.hitbox.x + player.hitbox.width / 2;
        float playerCenterY = player.hitbox.y + player.hitbox.height / 2;
        
        // Euclidean distance check
        float dx = playerCenterX - enemyCenterX;
        float dy = playerCenterY - enemyCenterY;
        float distance = (float)Math.sqrt(dx * dx + dy * dy);
        return distance <= attackDistance * 7;
    }
    
    protected boolean isPlayerCloseForAttack(Player player) {
    	float enemyCenterX = hitbox.x + hitbox.width / 2;
        float enemyCenterY = hitbox.y + hitbox.height / 2;
        float playerCenterX = player.hitbox.x + player.hitbox.width / 2;
        float playerCenterY = player.hitbox.y + player.hitbox.height / 2;
        
        // Euclidean distance check
        float dx = playerCenterX - enemyCenterX;
        float dy = playerCenterY - enemyCenterY;
        float distance = (float)Math.sqrt(dx * dx + dy * dy);
        return distance <= attackDistance;
    }

    protected void newState(int newEnemyState) {
        if (this.enemyState != newEnemyState) { // Check if state is actually changing
            this.enemyState = newEnemyState;
            aniTick = 0;
            aniIndex = 0;
        }
    }
    
    public void hurt(int amount) {
    	currentHealth -= amount;
    	if(currentHealth <= 0)
    		newState(DEATH);
    	else
    		newState(HURT);
    }
    
    protected void checkEnemyHit(Rectangle2D.Float attackBox, Player player) {
		if(attackBox.intersects(player.hitbox))
			player.changeHealth(-GetEnemyDmg(enemyType));
		attackChecked = true;
		
	}
    
    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
                aniIndex = 0;

                switch (enemyState) {
                    case ATTACKING, HURT -> {
                        enemyState = IDLE;
                        isAttacking = false;
                    }
                    case DEATH -> active = false;
                }
            }
        }
    }
    
 // The protected move method encapsulates the movement logic
    protected void move(int[][] lvlData, float xSpeed, float ySpeed) {
        // Attempt to move in the desired direction.
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y + ySpeed, hitbox.width, hitbox.height, lvlData)) {
            // For horizontal movement, incorporate an additional floor check.
            if (xSpeed != 0) {
                hitbox.x += xSpeed;
                if (IsFloor(hitbox, xSpeed, lvlData)) {
                    hitbox.x += xSpeed;
                }
            } else {
                // Vertical movement.
                hitbox.y += ySpeed;
            }
            // Accumulate the traveled distance.
            currentPatrolDistance += Math.abs(xSpeed) + Math.abs(ySpeed);
        } else {
            // If movement is blocked, choose a new random direction and reset patrol distance.
            chooseRandomDirection();
            currentPatrolDistance = 0;
            return;
        }
        
        // When the traveled patrol distance exceeds the maximum allowed, choose a new direction.
        if (currentPatrolDistance >= maxPatrolDistance) {
            chooseRandomDirection();
            currentPatrolDistance = 0;
        }
    }
    
    // This method chooses a random direction for the enemy.
    protected void chooseRandomDirection() {
        int direction = rand.nextInt(4);
        switch (direction) {
            case 0:
                newState(RUNNING_UP);
                break;
            case 1:
            	newState(RUNNING_DOWN);
                break;
            case 2:
            	newState(RUNNING_LEFT);
                break;
            case 3:
                newState(RUNNING_RIGHT);
                break;
            default:
                break;
        }
    }
    
    public void resetEnemy() {
    	hitbox.x = x;
    	hitbox.y = y;
    	
    	currentHealth = maxHealth;
    	newState(IDLE);
    	active = true;
    	isAttacking = false;
    	
    }
    
    public int getAniIndex() {
        return aniIndex;
    }
    
    public int getEnemyState() {
        return enemyState;
    }
    
    public boolean isActive() {
    	return active;
    }
}
