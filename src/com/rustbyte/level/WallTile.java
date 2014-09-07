package com.rustbyte.level;

public class WallTile extends Tile {
	public WallTile(int x, int y, int wid, int hgt, Level lev) {
		super(x, y, wid, hgt, lev);
		this.typeID = 0x202020;
		this.blocking = false;
		this.tsetOffsetX = 22;
		this.tsetOffsetY = 1;
	}

}
