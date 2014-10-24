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
	public boolean sloped;		
	public int baseColor;
	protected int hitpoints = 100;
	protected int hurtTimer = 0;
	protected boolean flipTile = false;
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
		case 0xB70000: t = new RedBrickTile(tx,ty,tw,th,l); break;
		case 0xA80000: {
			t = new RedBrickTile(tx,ty,tw,th,l);
			t.sloped = true;
		} break;
		case 0x910000: t = new RedBrickInsideTile(tx,ty,tw,th,l); break;
		case 0x963C00: t = new WoodPlanksTile(tx,ty,tw,th,l); break;
		case 0x4C1E00: t = new WoodPlanksInsideTile(tx,ty,tw,th,l); break;
		case 0x202020: t = new WallTile(tx,ty,tw,th,l); break;
		case 0x7F6A00: t = new DoorTile(tx,ty,tw,th,l); break;
		case 0x00FF21: // MOB_SPAWN_TYPE_HUMAN, fall through to skelly
		case 0xFF0000: // MOB_SPAWN_TYPE_ZOMBIE ----- || ----------
		case 0xFFFFFF: t = new MobSpawnerTile(typeID,tx,ty,tw,th,l); break; // Skelly spawn
		case 0x7F006E: t = new BossSpawnerTile(typeID,tx,ty,tw,th,l); break;
		case 0x0026FF: t = new RescueZoneTile(tx,ty,tw,th,l); break;		
		case 0xFFFF00: t = new PlayerSpawnTile(tx,ty,tw,th,l); break;		
		case 0x2D2D2D: t = new RockTile(tx,ty,tw,th,l); break;
		case 0x803401: t = new DirtTile(tx,ty,tw,th,l); break;
		case 0x0094FF: t = new LevelExitTile(tx,ty,tw,th,l); break;
		default: {
			System.out.println("Unknown tile type. TypeID: " + typeID + " tx: " + tx + " ty: " + ty);
		}break;
		}
		return t;
	}
	public void draw(Bitmap dest, int xx, int yy) {
		if( tsetOffsetX + tsetOffsetY == 0 ) 
			return;
		Art.tiles.draw(dest, xx, yy, tsetOffsetX, tsetOffsetY, width, height, flipTile);		
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
	
	public void interact(Entity e) {
		// Override in subclasses.
	}

	protected void addImpactParticles(Entity source, int amount) {
		double px = (this.tx * this.width) + this.halfWidth + (this.halfWidth * -source.facing);
		double py = source.yy;	
		ParticleEmitter pe = new ParticleEmitter(px, py, -(double)source.facing, -1.0, 1, 5, baseColor, null, level.game);
		level.game.addEntity(pe);		
	}
	
	@Override
	public void takeDamage(Entity source, int amount) {		
		addImpactParticles(source, amount);
	}
}