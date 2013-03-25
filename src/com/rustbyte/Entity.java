package com.rustbyte;

import java.util.ArrayList;
import java.util.List;

import com.rustbyte.Game;
import com.rustbyte.level.*;

public abstract class Entity {
	public double xx;
	public double yy;
	public int dirX;
	public int dirY;
	public double velX;
	public double velY;
	public int wid;
	public int hgt;
	public double xr = 8.0;
	public double yr = 9.0;
	public double speed = 0.1;
	public int facing = 1;
	public boolean alive = false;			
	public boolean onground = true;
	public boolean ignoresGravity = false;	
	
	protected Game game;
	public SpriteAnimator animator;
	private int tileID = 0;
	
	protected Entity parent = null;
	protected List<Entity> subEntities = new ArrayList<Entity>();
	
	public Entity(double x, double y, int w, int h, Entity p,  Game g) {
		this.xx = x;
		this.yy = y;
		this.wid = w;
		this.hgt = h;
		this.animator = new SpriteAnimator();
		this.game = g;
		this.parent = p;
	}
	public void applyGravity(double gravity) {
		if(!ignoresGravity) {
			velY += gravity;
			if(velY > 7)
				velY = 7;			
		}
	}
	public void postTick() {
		if(!this.alive) {			
			game.level.getTileFromID(tileID).removeEntity(this);
		} else {		
			Tile curTile = game.level.getTileFromPoint(xx, yy);
			if( curTile != null) {
				if(curTile.tileID != tileID) {
					game.level.getTileFromID(tileID).removeEntity(this);
					curTile.addEntity(this);
					tileID = curTile.tileID;
				}
			}
		}
	}
	public void tick() {
		for(int i=0; i < subEntities.size(); i++) {
			Entity ent = subEntities.get(i);
			ent.tick();
		}
	}
	public abstract void render();
	public abstract void move();	
}
