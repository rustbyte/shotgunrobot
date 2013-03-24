package com.rustbyte;

import java.util.ArrayList;
import java.util.List;

public class Tile implements Destructable {
	public static final Tile TILE_NONE = new Tile(0x498FFF, false, Art.getColor(255,255,255), 0,0);
	public static final Tile TILE_BRICK = new Tile(0x404040, true, Art.getColor(180,180,180), 51, 1);
	public static final Tile TILE_WALL = new Tile(0x202020, false, Art.getColor(32,32,32), 72, 1);
	public static final Tile TILE_DOOR_LEFT = new Tile(0x7F6A00, true, 0x7F6A00, 51, 72 );
	public static Tile[] tileTypes = {
		TILE_NONE,
		TILE_BRICK,
		TILE_WALL,
		TILE_DOOR_LEFT
	};
	
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
	private Level level;
	private List<Entity> entities = new ArrayList<Entity>();
	
	private Tile(int id, boolean b, int col, int ox, int oy) {
		this.typeID = id;
		this.blocking = b;
		this.baseColor = col;
		this.tsetOffsetX = ox;
		this.tsetOffsetY = oy;
	}
	
	public Tile(int x, int y, int wid, int hgt, Tile type, Level lev) {
		this.tx = x;
		this.ty = y;
		this.width = wid;
		this.height = hgt;
		this.halfWidth = wid / 2;
		this.halfHeight = hgt / 2;
		this.typeID = type.typeID;
		this.blocking = type.blocking;
		this.level = lev;
		this.baseColor = type.baseColor;
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
	public static Tile getTileTypeFromID(int id) {
		for(int i=0; i < tileTypes.length; i++)
			if(tileTypes[i].typeID == id)
				return tileTypes[i];
		return null;
	}

	@Override
	public void takeDamage(Entity source, int amount) {		
		double px = (this.tx * this.width) + this.halfWidth + (this.halfWidth * -source.facing);
		double py = source.yy;	
		ParticleEmitter pe = new ParticleEmitter(px, py, -(double)source.facing, -1.0, 1, 5, 0x000000, null, level.game);
		level.game.addEntity(pe);		
	}
}