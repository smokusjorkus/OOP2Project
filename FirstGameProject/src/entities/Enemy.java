package entities;

import static utils.Constants.EnemyConstants.*;
import static utils.HelpMethods.*;

import main.Game;

import static utils.Constants.Directions.*;
import java.util.Random;

public abstract class Enemy extends Entity {
    private int aniIndex, enemyState, enemyType;
    private int aniTick, aniSpeed = 25;
    private float walkSpeed = 0.3f * Game.SCALE;

    // Maximum distance the enemy will travel before changing to a random direction.
    private float maxPatrolDistance = 100 * Game.SCALE;
    // Accumulates the distance traveled during the current patrol.
    private float currentPatrolDistance = 0;

    private Random rand = new Random();

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitbox(x, y, width, height);

        enemyState = RUNNING_UP;
    }
    
    private void updateAnimationTick() {
        aniTick++;
        if(aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if(aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
                aniIndex = 0;
            }
        }
    }
    
    public void update(int[][] lvlData) {
        updateMove(lvlData);
        updateAnimationTick();
    }
    
    public void updateMove(int[][] lvlData) {
        // xSpeed and ySpeed represent the intended speed in horizontal and vertical directions.
        float xSpeed = 0;
        float ySpeed = 0;
        
        // Determine movement based on the enemy state.
        switch (enemyState) {
            case RUNNING_LEFT:
                xSpeed = -walkSpeed;
                break;
            case RUNNING_RIGHT:
                xSpeed = walkSpeed;
                break;
            case RUNNING_UP:
                ySpeed = -walkSpeed;
                break;
            case RUNNING_DOWN:
                ySpeed = walkSpeed;
                break;
            default:
                break;
        }
        
        // Attempt to move in the desired direction.
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y + ySpeed, hitbox.width, hitbox.height, lvlData)) {
            // For horizontal movement, incorporate additional floor checks.
            if (enemyState == RUNNING_LEFT || enemyState == RUNNING_RIGHT) {
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
            // If movement is blocked, change to a random direction and reset patrol distance.
            chooseRandomDirection();
            currentPatrolDistance = 0;
            return;
        }
        
        // When the traveled patrol distance exceeds the maximum allowed, choose a new random direction.
        if (currentPatrolDistance >= maxPatrolDistance) {
            chooseRandomDirection();
            currentPatrolDistance = 0;
        }
    }
    
    // This method chooses a random direction for the enemy.
    private void chooseRandomDirection() {
        int direction = rand.nextInt(4);
        switch (direction) {
            case 0:
                enemyState = RUNNING_UP;
                break;
            case 1:
                enemyState = RUNNING_DOWN;
                break;
            case 2:
                enemyState = RUNNING_LEFT;
                break;
            case 3:
                enemyState = RUNNING_RIGHT;
                break;
            default:
                break;
        }
    }
    
    public int getAniIndex() {
        return aniIndex;
    }
    
    public int getEnemyState() {
        return enemyState;
    }
}
