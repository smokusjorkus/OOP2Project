package entities;

import java.awt.Graphics;
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

	public void update(int[][] lvlData) {
		for(Slime b : slime)
			b.update(lvlData);
	}
	
	public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
		drawSlimes(g, xLvlOffset, yLvlOffset);
		for(Slime b : slime)
			b.drawHitbox(g, xLvlOffset, yLvlOffset);
	}
	
	private void drawSlimes(Graphics g, int xLvlOffset, int yLvlOffset) {
		for(Slime b : slime)
			g.drawImage(slimeArr[b.getEnemyState()][b.getAniIndex()], (int) (b.getHitbox().x - SLIME_DRAWOFFSET_X) - xLvlOffset, (int) (b.getHitbox().y - SLIME_DRAWOFFSET_Y) - yLvlOffset, SLIME_WIDTH, SLIME_HEIGHT, null);
		
	}

	private void loadEnemyImgs() {
		slimeArr = new BufferedImage[20][10];
		BufferedImage temp = LoadSave.GetSpriteAtlas("slime.png");
		for(int j = 0; j < slimeArr.length; j++)
			for(int i = 0; i < slimeArr[j].length; i++)
				slimeArr[j][i] = temp.getSubimage(i * SLIME_WIDTH_DEFAULT, j * SLIME_HEIGHT_DEFAULT, SLIME_WIDTH_DEFAULT, SLIME_HEIGHT_DEFAULT);
		
	}
}
