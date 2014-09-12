package com.rustbyte.level;

import com.rustbyte.Skeleton;

public class SkeletonSpawnerTile extends Tile {
	public SkeletonSpawnerTile(int x, int y, int wid, int hgt, Level lev) {
		super(x, y, wid, hgt, lev);
		
		this.blocking = false;
		this.tsetOffsetX = 43;
		this.tsetOffsetY = 1;
	}
	
	public void init() {
		for(int i=0; i < 5; i++) {
			level.game.addEntity( new Skeleton( tx * 20, ty * 20, 20, 20, null, level.game));
		}
	}
}
