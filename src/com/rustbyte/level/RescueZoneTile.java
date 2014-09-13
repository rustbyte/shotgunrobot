package com.rustbyte.level;

import com.rustbyte.level.Tile;

public class RescueZoneTile extends Tile {
	public RescueZoneTile(int x, int y, int wid, int hgt, Level lev) {
		super(x, y, wid, hgt, lev);
		this.baseColor = 0x0026FF;
		this.tsetOffsetX = 22;
		this.tsetOffsetY = 43;
	}

	public void init() {
		
		int offset = 0;
		Tile leftTile = level.getTile(tx-1,ty);
		if(leftTile instanceof RedBrickInsideTile) offset = 127;
		if(leftTile instanceof WallTile) offset = 85;
		
		for(int x=0; x < 2; x++) {
			for(int y=0; y < 2; y++) {
				Tile t = level.getTile(tx+x, (ty-1)+y);
				t.tsetOffsetX = offset + (x * 20 + (x * 1));
				t.tsetOffsetY = 64 + (y * 20 + (y * 1));
			}
		}
	}
}
