package com.rustbyte.level;

public class RedBrickInsideTile extends Tile {
	public RedBrickInsideTile(int x, int y, int wid, int hgt, Level lev) {
		super(x, y, wid, hgt, lev);
		this.typeID = 0x910000;
		this.blocking = false;
		this.tsetOffsetX = 72;
		this.tsetOffsetY = 85;
	}
}
