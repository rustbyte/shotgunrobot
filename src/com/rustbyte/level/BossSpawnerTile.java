package com.rustbyte.level;

import java.util.Random;
import com.rustbyte.BatBossMobFactory;
import com.rustbyte.Mob;
import com.rustbyte.MobFactory;
import com.rustbyte.Player;
import com.rustbyte.vector.Vector2;

public class BossSpawnerTile extends Tile {
	private static final int BOSS_SPAWN_TYPE_BAT = 0x7F006E;
	
	private boolean activated = false;
	private int bossActivationTimer = 0;
	private int nextMobTimer = 0;
	private int maxMobs = 1;
	private int numMobsSpawned = 0;
	private int spawnType = 0;
	private Mob boss = null;
	private MobFactory mobFactory;
	
	public BossSpawnerTile(int type, int x, int y, int wid, int hgt, Level lev) {
		super(x, y, wid, hgt, lev);
		this.baseColor = 0xFF0000;
		this.tsetOffsetX = 1;
		this.tsetOffsetY = 64;
		this.spawnType = type;
		
		switch(this.spawnType) {
		case BOSS_SPAWN_TYPE_BAT: 
			mobFactory = BatBossMobFactory.getInstance();
			break;
		}
	}
	
	private void spawnMob() {
		
		if( nextMobTimer <= 0 && numMobsSpawned < maxMobs)					
			level.game.addEntity( boss );
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
				
		// Set proper background tile
		Tile[] tlist = new Tile[] {
			level.getTile(tx-1, ty),
			level.getTile(tx+1, ty)
		};				
		
		for(int i=0; i < tlist.length; i++) {
			Tile t = tlist[i];
			if(t != null) {
				if(t.typeID == 0x202020) {			
					tsetOffsetX = 85;
					tsetOffsetY = 1;
					break;
				}
				if(t.typeID == 0x910000) {
					tsetOffsetX = 106;
					tsetOffsetY = 1;
					setSpawnBackground(85, 127);
					break;
				}
				if(t.typeID == 0x4C1E00) {
					tsetOffsetX = 127;
					tsetOffsetY = 1;
				}
			}
		}
				
	}	
	
	private void setSpawnBackground(int backOffsetX, int backOffsetY) {
		
		for(int x=0; x < 3; x++ ) {
			for(int y=0; y < 3; y++ ) {
				Tile t = level.getTile((tx - 1) + x, (ty - 1) + y);
				t.tsetOffsetX = backOffsetX + ( x * 21);
				t.tsetOffsetY = backOffsetY + ( y * 21);
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
			if( distance < 200) {
				// Activate!
				activated = true;
				bossActivationTimer = 500;
				//boss = Mob.createMob(mobName, (tx * 20) + 10, (ty * 20) + 10,20,20,null, level.game);
				//boss = mobFactory.spawnMob((tx * 20) + 10, (ty * 20) + 10,20,20,null, level.game);
				level.game.activateBoss(boss);
			}
		} else {
			if( --bossActivationTimer < 0 ) 
				bossActivationTimer = 0;
		}
				
		if( activated && bossActivationTimer <= 0) {
			spawnMob();
		}
	}
}
