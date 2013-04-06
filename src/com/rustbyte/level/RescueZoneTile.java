package com.rustbyte.level;

import com.rustbyte.level.Tile;

public class RescueZoneTile extends Tile {
	public RescueZoneTile(int x, int y, int wid, int hgt, Level lev) {
		super(x, y, wid, hgt, lev);
		this.baseColor = 0x0026FF;
		this.tsetOffsetX = 72;
		this.tsetOffsetY = 43;
	}

}
