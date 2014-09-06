package com.rustbyte.level;

import com.rustbyte.Art;

public class WoodPlanksInsideTile extends Tile {
	public WoodPlanksInsideTile(int x, int y, int wid, int hgt, Level lev) {
		super(x, y, wid, hgt,  lev);
		this.typeID = 0x4C1E00;
		this.blocking = false;
		this.baseColor = Art.getColor(150,60, 0);
		this.tsetOffsetX = 72;
		this.tsetOffsetY = 106;
	}
}
