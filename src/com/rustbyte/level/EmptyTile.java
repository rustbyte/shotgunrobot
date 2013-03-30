package com.rustbyte.level;

import com.rustbyte.Art;

public class EmptyTile extends Tile {
	public EmptyTile(int x, int y, int wid, int hgt, Level lev) {
		super(x, y, wid, hgt, lev);
		this.typeID = 0x498FFF;
		this.blocking = false;
		this.baseColor = Art.getColor(255,255,255);
		this.tsetOffsetX = 0;
		this.tsetOffsetY = 0;
	}

}
