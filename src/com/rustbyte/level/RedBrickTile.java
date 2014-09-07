package com.rustbyte.level;

import com.rustbyte.Art;

public class RedBrickTile extends Tile {
	public RedBrickTile(int x, int y, int wid, int hgt, Level lev) {
		super(x, y, wid, hgt, lev);
		this.typeID = 0xB70000;
		this.blocking = true;
		this.baseColor = Art.getColor(255,255,255);
		this.tsetOffsetX = 22;
		this.tsetOffsetY = 64;
	}
}
