package com.rustbyte;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.rustbyte.Art;
import com.rustbyte.Bitmap;
import com.rustbyte.Entity;
import com.rustbyte.InputHandler;
import com.rustbyte.Level;
import com.rustbyte.Player;
import com.rustbyte.Zombie;

public class Game {
	public int WIDTH;
	public int HEIGHT;
	public Bitmap screen;
	private double gravity = 1;
	
	public InputHandler input;
	public Level level;
	public Player player;
	private List<Entity> entities = new ArrayList<Entity>();
	public int tickcount = 0;	
	public int FPS = 0;
	
	// parallax background
	private int numLayers = 3;
	private BackgroundLayer[] layers = new BackgroundLayer[numLayers];	
	
	public Game(int wid, int hgt) {
		this.WIDTH = wid;
		this.HEIGHT = hgt;
		this.screen = new Bitmap(WIDTH, HEIGHT);
		
		input = new InputHandler();
		level = new Level(Art.level1, 20,20, this);
		player = new Player(30, 30, 20, 20, null, this);
		player.alive = true;
		
		level.viewX = 0;
		level.viewY = 0;
		level.viewWidth = WIDTH;
		level.viewHeight = HEIGHT;
		//addEntity(player);
		Random rand = new Random();
		int xx = 40;
		int nummobs = 50;
		if(nummobs > 0) {
			int minspacing = WIDTH / nummobs;
			for(int i=0; i < nummobs; i++) {
				xx += minspacing + rand.nextInt(40);
				if(xx > (level.width * level.tileWidth) - 40)
					xx = 40;
				if( xx >= ((level.width * level.tileWidth) - 40))
					xx = (level.width * level.tileWidth) - 40;			
				addEntity(new Zombie(xx,50, 20, 20, null ,this));
			}	
		}
		
		layers[0] = new BackgroundLayer(Art.background, 0, 100);
		layers[1] = new BackgroundLayer(Art.background, 100, 50);		
		layers[2] = new BackgroundLayer(Art.background, 150, 90);		
	}
	
	public void addEntity(Entity ent) {
		ent.alive = true;
		entities.add(ent);
	}
	
	public void tick() {
		tickcount++;
		
		gravity = 0.25;		
		for(int i=0; i < entities.size(); i++) { 
			Entity ent = entities.get(i);		
			if(ent.alive) {
				ent.applyGravity(gravity);
				ent.tick();
				ent.postTick();
			} else {
				entities.remove(ent);
			}
		}
		if(player.alive) { 
			player.applyGravity(gravity);
			player.tick();
			player.postTick();		
		}
		
		level.tick();
		level.setViewPos((int)player.xx, (int)player.yy);
		
		if( level.viewX > 0 && (level.viewX + level.viewWidth) < level.tileWidth * level.width) {
			for(int i=0; i<numLayers;i++) {				
				double layerSpeed = player.velX / (numLayers - i);
				layers[i].move(-layerSpeed * 1.1);				
			}
		}			
	}
	
	public void render() {
		for(int i=0; i < WIDTH * HEIGHT;i++)
			screen.pixels[i] = 0x498FFF;
		
		// render background
		for(int i=0; i < numLayers;i++) {
			BackgroundLayer layer = layers[i];
			layer.draw(screen);
		}
		
		level.draw(screen);
				
		screen.drawText(Art.font, "fps: " + FPS, 0,0,0xFFFF00, true);
		if(player.alive)
			screen.drawText(Art.font, "HP: " + player.hitpoints, 0,10,0xFFFF00, true);
		else
			screen.drawText(Art.font, "PLAYER DEAD!!", 0,10,0xFFFF00, true);
		
		for(int i=0; i < entities.size();i++) {
			Entity ent = entities.get(i);
			if( ent.alive ) {
				if( (ent.xx + ent.wid/2) < level.viewX || ( (ent.xx - ent.wid/2) > (level.viewX + level.viewWidth))) 
					continue;
				if( (ent.yy + ent.hgt/2) < level.viewY || ( (ent.yy - ent.hgt/2) > (level.viewY + level.viewHeight))) 
					continue;
				
				ent.render();
			}
		}
		
		if(player.alive)
			player.render();
	}
}
