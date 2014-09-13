package com.rustbyte.level;

public class RockTile extends Tile {

	public RockTile(int x, int y, int wid, int hgt, Level lev) {
		super(x, y, wid, hgt, lev);

		blocking = true;
		tsetOffsetX = 64;
		tsetOffsetY = 43;
	}

}
