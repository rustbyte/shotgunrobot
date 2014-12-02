package com.rustbyte;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.rustbyte.Art;
import com.rustbyte.Bitmap;
import com.rustbyte.Entity;
import com.rustbyte.InputHandler;
import com.rustbyte.PathFinder.Node;
import com.rustbyte.Player;
import com.rustbyte.Zombie;
import com.rustbyte.level.*;
import com.rustbyte.vector.Vector2;
import com.rustbyte.gui.Frame;
import com.rustbyte.gui.Label;

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
	private int playerDeadTimer = 0;
	
	// Player score stuff
	public int humansSaved = 0;
	public int humansLost = 0;
	public int zombiesKilled = 0;
	public int numZombies = 0;
	public int numHumans = 0;
	
	private boolean bossActivated = false;
	private Mob boss = null;
	
	// parallax background
	private int numLayers = 3;
	private BackgroundLayer[] layers = new BackgroundLayer[numLayers];	
	private ColorFadeEffect colorfadeEffect;
	private PathFinder pf;
	private boolean stepPathKeyPressed = false;
	
	private Frame playerHUD;
	private Label playerHealthStatusLabel;
	private Label playerMissionStatusLabel;
	
	private Frame testFrame;
	private Label bossStatusLabel;
	
	public Game(int wid, int hgt) {
		this.WIDTH = wid;
		this.HEIGHT = hgt;
		this.screen = new Bitmap(WIDTH, HEIGHT);
		colorfadeEffect = new ColorFadeEffect(0,WIDTH,HEIGHT);
		
		input = new InputHandler();
		level = new Level(Art.level2, 20,20, this);
				
		level.viewX = 0;
		level.viewY = 0;
		level.viewWidth = WIDTH;
		level.viewHeight = HEIGHT;
		Random rand = new Random();
		int xx = 100;
		int nummobs = 0;
		int humanCount = 0;
		if(nummobs > 0) {
			int minspacing = WIDTH / nummobs;
			for(int i=0; i < nummobs; i++) {
				xx += minspacing + rand.nextInt(40);
				if(xx > (level.width * level.tileWidth) - 40)
					xx = 100;
				if( xx >= ((level.width * level.tileWidth) - 40))
					xx = (level.width * level.tileWidth) - 40;
				if(humanCount < (nummobs / 2)) {
					addEntity(new Human(xx,100, 20, 20, null ,this));
					humanCount++;
				} else {
					addEntity(new Zombie(xx,100, 20, 20, null ,this));
				}
				//addEntity( Powerup.createPowerup(  1 + rand.nextInt(3), xx, 100, null, this));
			}	
		}			
				
		
		//addEntity(new Zombie( ((level.width - 5) * 20) - 10, 120,20,20,null, this));
		//addEntity(new Human( ((level.width - 10) * 20) - 10, 120,20,20,null,this));
		
		
		/*
		addEntity(new Powerup( ((level.width /2 ) * 20) - 10, 120,20,20, 
				  Powerup.POWERUP_TYPE_BATTERY, null, this));

		addEntity(new Powerup( ((level.width /2 ) * 20) - 30, 120,20,20, 
				  Powerup.POWERUP_TYPE_SHELLS, null, this));

		addEntity(new Powerup( ((level.width /2 ) * 20) - 50, 120,20,20, 
				  Powerup.POWERUP_TYPE_GRENADES, null, this));
		*/
		
		
		
		layers[0] = new BackgroundLayer(Art.background, 0, 100);
		layers[1] = new BackgroundLayer(Art.background, 100, 50);		
		layers[2] = new BackgroundLayer(Art.background, 150, 90);
		
		//constructPath();
		//pf = new PathFinder(level);
		//pf.initSearch(1, 19, 61 , 20);
		//pf.initSearch(1, 1, 18 , 14);
		
		respawnPlayer();
		
		//addEntity(new Skeleton( player.xx + 100, player.yy, 20, 20, null, this));
		//addEntity(new Zombie( (int)player.xx + 140, (int)player.yy, 20, 20, null, this));
		//addEntity(new BatBoss( ((level.width * 20) / 2) + 20, 100, 62,41, null, this));
		
		testFrame = new Frame( WIDTH - 105, 2, 80, 15);
		testFrame.addControl( (bossStatusLabel = new Label("",0,0, 0xFFFFFF)) );
		testFrame.init();
		
		playerHUD = new Frame( 20, 30, 120, 27);
		playerHUD.addControl( (playerHealthStatusLabel = new Label("HP: 999", 10, 7, 0xFFFF00)) );
		playerHUD.addControl( (playerMissionStatusLabel = new Label("{FFFF00}Mission: {00FF00}" + 
																	 "99 {FFFF00}/ {FF0000}" + 
																	 "99 {FFFF00}/ {0000FF}99", 
																	 10, 17,0xFFFF00)) );
		playerHUD.init();
	}
	
	public void activateBoss(Mob bossMob) {
		boss = bossMob;
		bossActivated = true;
	}
	
	public boolean isBossDefeated() {
		return (bossActivated && boss.hitpoints <= 0);
	}
	
	private void renderPath() {
		Iterator<Entity> iter = entities.iterator();
		while(iter.hasNext()) {
			iter.next();
			iter.remove();
		}
		
		//List<Node> p = pf.findPath(2, 3, 6, 3);		
		//List<Node> p = pf.findPath(1, 19, 61 , 21 );
		//List<Node> p = pf.findPath(1,19,18,14);
		List<Node> p = pf.closed;
		for(int i=0; i < p.size(); i++) {
			Node n = p.get(i);
			FloatingText ft = new FloatingText("" + n.costFromStart, 0xFFFF00, n.tile.tx * 20, n.tile.ty * 20, new Vector2(0,0), null, this);
			FloatingText ft2 = new FloatingText("" + n.costToGoal, 0x00FF00, n.tile.tx * 20, n.tile.ty * 20 + 8, new Vector2(0,0), null, this);
			addEntity(ft);
			addEntity(ft2);
		}
		List<Node> p2 = pf.open;
		for(int i=0; i < p2.size(); i++) {
			Node n = p2.get(i);
			FloatingText ft = new FloatingText("" + n.costFromStart, 0xFF0000, n.tile.tx * 20, n.tile.ty * 20, new Vector2(0,0), null, this);
			FloatingText ft2 = new FloatingText("" + n.costToGoal, 0x0000FF, n.tile.tx * 20, n.tile.ty * 20 + 8, new Vector2(0,0), null, this);
			addEntity(ft);
			addEntity(ft2);
		}		
	}
	public void addEntity(Entity ent) {
		ent.alive = true;
		entities.add(ent);
		
		if( ent instanceof Human ) numHumans++;
		if( ent instanceof Zombie ) numZombies++;
	}
	
	public void respawnPlayer() {
		
		Tile playerSpawn = level.getPlayerSpawn();
		int spawnX = (playerSpawn.tx * 20) + 10;
		int spawnY = (playerSpawn.ty * 20) + 10;
		if(player == null)
			player = new Player(0, 0, 20, 20, null, this);
		
		player.xx = spawnX;
		player.yy = spawnY;
		player.velocity = new Vector2();
		player.dirX = 0;
		player.alive = true;	
		player.hitpoints = 100;		
		playerDeadTimer = 0;		
	}
	public void tick() {
		tickcount++;
		
		input.tick();
		
		/*if(input.keys[KeyEvent.VK_F].pressed && !stepPathKeyPressed) {
			stepPathKeyPressed = true;
			pf.step();
		} else {
			if(!input.keys[KeyEvent.VK_F].pressed)
				stepPathKeyPressed = false;
		}*/					
		
		if(!player.alive && playerDeadTimer >= 100) {
			if( input.keys[KeyEvent.VK_SPACE].pressed )
				respawnPlayer();
		}

		gravity = 0.15;	
		
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
		} else {
			if(++playerDeadTimer > 100)
				playerDeadTimer = 100;
		}
		
		level.tick();		
		level.setViewPos((int)player.xx, (int)player.yy);
		if(player.alive) {
			if( level.viewX > 0 && (level.viewX + level.viewWidth) < level.tileWidth * level.width) {
				for(int i=0; i<numLayers;i++) {				
					double layerSpeed = player.velocity.x / (numLayers - i);
					layers[i].move(-layerSpeed * 1.1);				
				}
			}			
		}
	}
	
	public void render() throws Exception {
		/*for(int i=0; i < WIDTH * HEIGHT;i++)
			screen.pixels[i] = 0x498FFF;*/
		
		// render background
		for(int i=0; i < numLayers;i++) {
			BackgroundLayer layer = layers[i];
			layer.draw(screen);
		}		
		level.draw(screen);
				
		//screen.drawText(Art.font, "fps: " + FPS, 0,0,0xFFFF00, true);
		
				
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
		
		if(player.alive) {
			playerHealthStatusLabel.setText("HP: " + player.hitpoints);
			playerMissionStatusLabel.setText("{FFFF00}Mission: {00FF00}" + 
											humansSaved + " {FFFF00}/ {FF0000}" + 
											humansLost + " {FFFF00}/ {0000FF}" + zombiesKilled);
			
			playerHUD.render(screen, 0, 0);
			/*if(bossActivated) {
				testFrame.render(screen, 0, 0);
				screen.drawText(Art.font, "{FF0000}ENEMY: {FFFFFF}" + (boss.hitpoints < 0 ? 0 : boss.hitpoints) , 
								screen.width - 100, 6, 0xFFFFFF, true );
			}*/
			
			player.render();
		} else {
			if(playerDeadTimer >= 100) {				
				colorfadeEffect.clear();
				screen.draw(colorfadeEffect.renderFrame, 0, 0);
				colorfadeEffect.render(tickcount, screen, 0, 0);
				screen.drawText(Art.font, "YOU HAVE FAILED!", WIDTH / 2 - 40, HEIGHT / 2 - 20,0xFFFF00, true);
				screen.drawText(Art.font, "press space to continue...", WIDTH / 2 - 50, HEIGHT / 2,0xFFFF00, true);
			}
		}		
	}
}
