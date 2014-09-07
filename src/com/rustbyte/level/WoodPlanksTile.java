package com.rustbyte.level;

import com.rustbyte.Art;

public class WoodPlanksTile extends Tile {
	public WoodPlanksTile(int x, int y, int wid, int hgt, Level lev) {
		super(x, y, wid, hgt,  lev);
		this.typeID = 0x963C00;
		this.blocking = false;
		this.baseColor = Art.getColor(150,60, 0);
		this.tsetOffsetX = 1;
		this.tsetOffsetY = 106;
	}
}
