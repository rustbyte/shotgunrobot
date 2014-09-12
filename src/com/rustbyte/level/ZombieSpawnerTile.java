package com.rustbyte.level;

import com.rustbyte.Zombie;
import com.rustbyte.level.Tile;
import com.rustbyte.Player;
import com.rustbyte.vector.Vector2;

public class ZombieSpawnerTile extends Tile {
	private boolean activated = false;
	private int waveActivationTimer = 0;
	private int nextZombieTimer = 0;
	private int maxZombies = 20;
	private int numZombiesSpawned = 0;
	
	public ZombieSpawnerTile(int x, int y, int wid, int hgt, Level lev) {
		super(x, y, wid, hgt, lev);
		this.baseColor = 0xFF0000;
		this.tsetOffsetX = 1;
		this.tsetOffsetY = 64;
	}
	
	private void spawnZombie() {
		if( nextZombieTimer <= 0 && numZombiesSpawned < maxZombies) {
			level.game.addEntity(new Zombie((tx * 20) + 10, (ty * 20) + 10,20,20,null, level.game));
			nextZombieTimer = 60;
			numZombiesSpawned++;
		}
		if( --nextZombieTimer < 0 ) 
			nextZombieTimer = 0;
	}
	@Override
	public void init() {			

	}	
	@Override
	public void tick() {
		if( !activated ) {
			// Check distance to player.
			Player p = level.game.player;
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
				
		if( activated && waveActivationTimer <= 0) {
			spawnZombie();
		}			
	}
}
