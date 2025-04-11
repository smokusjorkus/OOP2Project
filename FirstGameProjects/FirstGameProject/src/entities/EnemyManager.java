package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import utils.LoadSave;
import static utils.Constants.EnemyConstants.*;

public class EnemyManager {
	
	private Playing playing;
	private BufferedImage[][] slimeArr;
	private ArrayList<Slime> slime = new ArrayList<>();
	
	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
		addEnemies();
	}
	
	private void addEnemies() {
		slime = LoadSave.GetSlimes("levelOne_layer2.png");
		
	}

	public void update(int[][] lvlData, Player player) {
		for(Slime s : slime)
			if(s.isActive())
				s.update(lvlData, player);
	}
	
	public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
		drawSlimes(g, xLvlOffset, yLvlOffset);
//		for(Slime b : slime)
//			b.drawAttackBox(g, xLvlOffset, yLvlOffset);
	}
	
	private void drawSlimes(Graphics g, int xLvlOffset, int yLvlOffset) {
		for(Slime s : slime)
			if(s.isActive()) {
				g.drawImage(slimeArr[s.getEnemyState()][s.getAniIndex()], (int) (s.getHitbox().x - SLIME_DRAWOFFSET_X) - xLvlOffset, (int) (s.getHitbox().y - SLIME_DRAWOFFSET_Y) - yLvlOffset, SLIME_WIDTH, SLIME_HEIGHT, null);
			}
	}
	
	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		for(Slime s : slime)
			if(s.isActive())
				if(attackBox.intersects(s.getHitbox())) {
					s.hurt(10);
				}
	}

	private void loadEnemyImgs() {
		slimeArr = new BufferedImage[20][10];
		BufferedImage temp = LoadSave.GetSpriteAtlas("slime.png");
		for(int j = 0; j < slimeArr.length; j++)
			for(int i = 0; i < slimeArr[j].length; i++)
				slimeArr[j][i] = temp.getSubimage(i * SLIME_WIDTH_DEFAULT, j * SLIME_HEIGHT_DEFAULT, SLIME_WIDTH_DEFAULT, SLIME_HEIGHT_DEFAULT);
		
	}
	
	public void resetAllEnemies() {
		for(Slime s : slime) {
			s.resetEnemy();
		}
	}
}
