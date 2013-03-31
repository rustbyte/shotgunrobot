package com.rustbyte.level;

import java.util.ArrayList;
import java.util.List;

import com.rustbyte.Art;
import com.rustbyte.Bitmap;
import com.rustbyte.Entity;
import com.rustbyte.Destructable;
import com.rustbyte.ParticleEmitter;

public class Tile implements Destructable {
	public int tileID;
	public int typeID;
	public int tx;
	public int ty;
	public int width;
	public int height;
	public int halfWidth;
	public int halfHeight;
	public int tsetOffsetX;
	public int tsetOffsetY;
	public boolean blocking;
	public int baseColor;
	protected int hitpoints = 100;
	protected int hurtTimer = 0;	
	public Level level;
	private List<Entity> entities = new ArrayList<Entity>();
	
	public Tile(int x, int y, int wid, int hgt, Level lev) {
		this.tx = x;
		this.ty = y;
		this.width = wid;
		this.height = hgt;
		this.halfWidth = wid / 2;
		this.halfHeight = hgt / 2;
		this.level = lev;
	}
	public void init() {
		// called by Level class after the map has been loaded.
	}
	public void tick() {		
	}
	public static Tile createTile(int typeID, int tx, int ty, int tw, int th, Level l) {
		Tile t = null;		
		switch(typeID) {
		case 0x498FFF: t = new EmptyTile(tx, ty, tw, th, l); break;
		case 0x404040: t = new BrickTile(tx,ty,tw,th,l); break;
		case 0x202020:
			t = new WallTile(tx,ty,tw,th,l); break;
		case 0x7F6A00: 
			t = new DoorTile(tx,ty,tw,th,l); break;		
		}
		return t;
	}
	public void draw(Bitmap dest, int xx, int yy) {
		Art.sprites.draw(dest, xx, yy, tsetOffsetX, tsetOffsetY, width, height);		
	}
	public void addEntity(Entity ent) {
		entities.add(ent);
	}
	public void removeEntity(Entity ent) {
		entities.remove(ent);
	}
	public ArrayList<Entity> getEntities() {
		return (ArrayList<Entity>)entities;
	}

	@Override
	public void takeDamage(Entity source, int amount) {		
		double px = (this.tx * this.width) + this.halfWidth + (this.halfWidth * -source.facing);
		double py = source.yy;	
		ParticleEmitter pe = new ParticleEmitter(px, py, -(double)source.facing, -1.0, 1, 5, baseColor, null, level.game);
		level.game.addEntity(pe);		
	}
}