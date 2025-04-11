package entities;

import static utils.Constants.EnemyConstants.*;
import static utils.HelpMethods.CanMoveHere;
import static utils.HelpMethods.IsFloor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;
public class Slime extends Enemy{
	private int idleTick = 0;
	private static final int IDLE_DURATION = 240; // assuming 60 FPS = 2 seconds
	
	//AttackBox
	private Rectangle2D.Float attackBox;
	private int attackBoxOffsetX, attackBoxOffsetY;

	public Slime(float x, float y) {
		super(x, y, SLIME_WIDTH, SLIME_HEIGHT, SLIME);
		initHitbox(x, y, (int) (22 * 1.2 * Game.SCALE),(int) (20 * 1.2 * Game.SCALE));
		initAttackBox();
	}
	
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x,y,(int)(82 * 1.2 * Game.SCALE),(int) (40 * 1.2 * Game.SCALE));
		attackBoxOffsetX = (int)(Game.SCALE * 30);
		attackBoxOffsetY = (int)(Game.SCALE * 20);
	}

	public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
    }
	
	private void updateAttackBox() {
		attackBox.x = hitbox.x - attackBoxOffsetX;
		attackBox.y = hitbox.y - attackBoxOffsetY;
	}

	public void updateBehavior(int[][] lvlData, Player player) {
	    float xSpeed = 0;
	    float ySpeed = 0;

	    // Only check for new states if NOT attacking
	    if (!isAttacking) {
	        if (isPlayerCloseForAttack(player)) {
	            newState(ATTACKING);
	            isAttacking = true;
	        } else if (canSeePlayer(lvlData, player)) {
	        	walkSpeed = 0.6f * Game.SCALE;
	            turnTowardsPlayer(player);
	        } else if (!canSeePlayer(lvlData, player)) {
	        	walkSpeed = 0.2f * Game.SCALE;
	        }
	    }

	    // Handle movement based on state
	    switch (enemyState) {
	    	case IDLE:
		        idleTick++;
		        if (idleTick >= IDLE_DURATION) {
		            idleTick = 0;
		            
		            // Choose a random direction to start moving
		            int dir = (int)(Math.random() * 4);
		            switch (dir) {
		                case 0 -> newState(RUNNING_LEFT);
		                case 1 -> newState(RUNNING_RIGHT);
		                case 2 -> newState(RUNNING_UP);
		                case 3 -> newState(RUNNING_DOWN);
		            }
		        }
		        break;
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
	        case ATTACKING:
	        	if(aniIndex == 0)
	        		attackChecked = false;
	        	
	            if(aniIndex == 6 && !attackChecked)
	            	checkEnemyHit(attackBox, player);
	            xSpeed = 0;
	            ySpeed = 0;
	            break;
	        default:
	            break;
	    }

	    move(lvlData, xSpeed, ySpeed);
	}
	

	public void drawAttackBox(Graphics g, int xLvlOffset, int yLvlOffset) {
		g.setColor(Color.red);
		g.drawRect((int)attackBox.x - xLvlOffset, (int)attackBox.y - yLvlOffset, (int)attackBox.width, (int)attackBox.height);
	}
	
}