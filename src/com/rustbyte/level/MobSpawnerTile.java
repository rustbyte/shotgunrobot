package com.rustbyte.level;

import java.util.Random;
import com.rustbyte.HumanMobFactory;
import com.rustbyte.Mob;
import com.rustbyte.MobFactory;
import com.rustbyte.Player;
import com.rustbyte.SkeletonMobFactory;
import com.rustbyte.ZombieMobFactory;
import com.rustbyte.vector.Vector2;

public class MobSpawnerTile extends Tile {
	private static final int MOB_SPAWN_TYPE_HUMAN = 0x00FF21;
	private static final int MOB_SPAWN_TYPE_ZOMBIE = 0xFF0000;	
	private static final int MOB_SPAWN_TYPE_SKELETON = 0xFFFFFF;
	
	private boolean activated = false;
	private int waveActivationTimer = 0;
	private int nextMobTimer = 0;
	private int maxMobs = 10;
	private int numMobsSpawned = 0;
	private int spawnType = 0;
	private String mobName;
	private MobFactory mobFactory;
	
	public MobSpawnerTile(int type, int x, int y, int wid, int hgt, Level lev) {
		super(x, y, wid, hgt, lev);
		this.baseColor = 0xFF0000;
		this.tsetOffsetX = 1;
		this.tsetOffsetY = 64;
		this.spawnType = type;
		
		switch(this.spawnType) {
		case MOB_SPAWN_TYPE_HUMAN: 
			mobFactory = HumanMobFactory.getInstance();
			break;
		case MOB_SPAWN_TYPE_ZOMBIE: 
			mobFactory = ZombieMobFactory.getInstance();
			break;
		case MOB_SPAWN_TYPE_SKELETON: 
			mobFactory = SkeletonMobFactory.getInstance();
			break;
		}
	}
	
	private void spawnMob() {
		
		if( nextMobTimer <= 0 && numMobsSpawned < maxMobs) {
			level.game.addEntity(mobFactory.spawnMob((tx * 20) + 10, (ty * 20) + 10,20,20,null, level.game)); 
			nextMobTimer = 60;
			numMobsSpawned++;
		}
		if( --nextMobTimer < 0 ) 
			nextMobTimer = 0;
	}
	@Override
	public void init() {			
		
		if(spawnType == MOB_SPAWN_TYPE_HUMAN) {
			Random rand = new Random();
			rand.setSeed(level.game.tickcount);
			
			for(int i = 0; i < 5 + rand.nextInt(5); i++) {
				level.game.addEntity(mobFactory.spawnMob(tx * 20, ty * 20,20,20,null, level.game));
			}			
		}		
				
		// Set proper background tile
		Tile[] tlist = new Tile[] {
			level.getTile(tx-1, ty),
			level.getTile(tx+1, ty)
		};				
		
		for(int i=0; i < tlist.length; i++) {
			Tile t = tlist[i];
			if(t != null) {
				if(spawnType == MOB_SPAWN_TYPE_HUMAN) {
					tsetOffsetX = t.tsetOffsetX;
					tsetOffsetY = t.tsetOffsetY;
				} else {
					if(t.typeID == 0x202020) {			
						tsetOffsetX = 85;
						tsetOffsetY = 1;
						break;
					}
					if(t.typeID == 0x910000) {
						tsetOffsetX = 106;
						tsetOffsetY = 1;
						break;
					}
					if(t.typeID == 0x4C1E00) {
						tsetOffsetX = 127;
						tsetOffsetY = 1;
					}
				}
			}
		}	
	}	
	@Override
	public void tick() {
		if( !activated ) {
			// Check distance to player.
			Player p = level.getPlayer();
			Vector2 playerPos = new Vector2(p.xx, p.yy);
			Vector2 myPos = new Vector2((this.tx * 20) + 10, (this.ty * 20) + 10);
			Vector2 delta = myPos.sub(playerPos);
			double distance = delta.length();
			if( distance < 80) {
				// Activate!
				activated = true;
				waveActivationTimer = 500;
			}
		} else {
			if( --waveActivationTimer < 0 ) 
				waveActivationTimer = 0;
		}
				
		if( activated && waveActivationTimer <= 0 && spawnType != MOB_SPAWN_TYPE_HUMAN) {
			spawnMob();
		}			
	}
}
