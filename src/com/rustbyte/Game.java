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
	private int numLayers = 5;
	private int layerHeight = 48;
	private BackgroundLayer[] layers = new BackgroundLayer[numLayers];	
	
	public Game(int wid, int hgt) {
		this.WIDTH = wid;
		this.HEIGHT = hgt;
		this.screen = new Bitmap(WIDTH, HEIGHT);
		
		input = new InputHandler();
		level = new Level(Art.level1, 20,20, this);
		player = new Player(30, 30, 20, 20, null, this);
		
		level.viewX = 0;
		level.viewY = 0;
		level.viewWidth = WIDTH;
		level.viewHeight = HEIGHT;
		//addEntity(player);
		Random rand = new Random();
		int xx = 40;
		int nummobs = 0;
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
		
		for(int i=0; i < numLayers;i++) {
			BackgroundLayer nextLayer = new BackgroundLayer();
			nextLayer.width = 320;
			nextLayer.height = layerHeight;
			nextLayer.x1 = 0;
			nextLayer.x2 = nextLayer.width;
			nextLayer.yy = layerHeight * i;
			layers[i] = nextLayer;
		}
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
		player.applyGravity(gravity);
		player.tick();
		player.postTick();
		
		//System.out.println("Active entities: " + entities.size());
		
		level.tick();
		level.setViewPos((int)player.xx, (int)player.yy);
		
		if( level.viewX > 0 && (level.viewX + level.viewWidth) < level.tileWidth * level.width) {
			for(int i=0; i<numLayers;i++) {
				double layerSpeed = 0;
				if( player.velX < 0) {
					layerSpeed = 1 + i;
				} else if ( player.velX > 0) {
					layerSpeed = -(1 + i);
				}				
				layers[i].move(layerSpeed);
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
		
		player.render();
	}
}
