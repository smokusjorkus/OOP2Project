package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import entities.Player;
import levels.LevelManager;
import main.Game;

public class Playing extends State implements Statemethods {
    private Player player;
    private LevelManager levelManager;
    
    // Camera tracking (now simpler since we center on player)
    private int xLvlOffset;
    private int yLvlOffset;
    
    // Level bounds
    private int lvlTilesWide;
    private int lvlTilesTall;
    private int maxLvlOffsetX;
    private int maxLvlOffsetY;
    
    public Playing(Game game) {
        super(game);
        initClasses();
        calcLevelBounds();
        centerCameraOnPlayer();
    }
    
    private void initClasses() {
        levelManager = new LevelManager(game);
        player = new Player(25*Game.TILES_SIZE, 25*Game.TILES_SIZE, (int) (192 * Game.SCALE), (int) (192 * Game.SCALE));
        player.loadLvlData(levelManager.getCurrentLevel().getLayers().get(3).getLevelData());
    }
    
    private void calcLevelBounds() {
        // Get dimensions from the first layer (assuming all layers have same dimensions)
        int[][] levelData = levelManager.getCurrentLevel().getLayers().get(3).getLevelData();
        lvlTilesWide = levelData[0].length;
        lvlTilesTall = levelData.length;
        
        maxLvlOffsetX = (lvlTilesWide * Game.TILES_SIZE) - Game.GAME_WIDTH;
        maxLvlOffsetY = (lvlTilesTall * Game.TILES_SIZE) - Game.GAME_HEIGHT;
    }

    @Override
    public void update() {
        levelManager.update();
        player.update();
        centerCameraOnPlayer();
    }

    private void centerCameraOnPlayer() {
        // Calculate desired camera position to center player
        int desiredX = (int) (player.getHitbox().x + player.getHitbox().width/2 - Game.GAME_WIDTH/2);
        int desiredY = (int) (player.getHitbox().y + player.getHitbox().height/2 - Game.GAME_HEIGHT/2);
        
        // Clamp camera position to level bounds
        xLvlOffset = Math.max(0, Math.min(desiredX, maxLvlOffsetX));
        yLvlOffset = Math.max(0, Math.min(desiredY, maxLvlOffsetY));
    }

    @Override
    public void draw(Graphics g) {
        levelManager.draw(g, xLvlOffset, yLvlOffset);
        player.render(g, xLvlOffset, yLvlOffset);
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1)
			player.setAttacking(true);
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
			player.setUp(true);
			break;
		case KeyEvent.VK_S:
			player.setDown(true);
			break;
		case KeyEvent.VK_A:
			player.setLeft(true);
			break;
		case KeyEvent.VK_D:
			player.setRight(true);
			break;
		case KeyEvent.VK_J:
			player.setAttacking(true);
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
			player.setUp(false);
			break;
		case KeyEvent.VK_S:
			player.setDown(false);
			break;
		case KeyEvent.VK_A:
			player.setLeft(false);
			break;
		case KeyEvent.VK_D:
			player.setRight(false);
			break;
		}
		
	}
	
	public void windowFocusLost() {
		player.resetDirBooleans();
	}

	public Player getPlayer() {
		return player;
	}
}
