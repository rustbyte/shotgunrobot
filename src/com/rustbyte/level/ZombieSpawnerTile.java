package com.rustbyte.level;

import com.rustbyte.Zombie;
import com.rustbyte.level.Tile;

public class ZombieSpawnerTile extends Tile {

	public ZombieSpawnerTile(int x, int y, int wid, int hgt, Level lev) {
		super(x, y, wid, hgt, lev);
		this.baseColor = 0xFF0000;
		this.tsetOffsetX = 51;
		this.tsetOffsetY = 64;
	}
	@Override
	public void init() {	
		/*
		for(int i = 0; i < 10; i++) {
			level.game.addEntity(new Zombie(tx * 20, (ty * 20) - 50,20,20,null, level.game));
		}*/
	}	
}
