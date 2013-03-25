package com.rustbyte.level;

import com.rustbyte.Art;

public class BrickTile extends Tile {

	public BrickTile(int x, int y, int wid, int hgt, Level lev) {
		super(x, y, wid, hgt,  lev);
		
		// new Tile(0x404040, true, Art.getColor(180,180,180), 51, 1);
		this.typeID = 0x404040;
		this.blocking = true;
		this.baseColor = Art.getColor(180,180,180);
		this.tsetOffsetX = 51;
		this.tsetOffsetY = 1;
	}

}
