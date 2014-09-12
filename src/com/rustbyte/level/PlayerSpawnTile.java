package com.rustbyte.level;

import com.rustbyte.Art;

public class PlayerSpawnTile extends Tile {
	public PlayerSpawnTile(int x, int y, int wid, int hgt, Level lev) {
		super(x,y,wid,hgt, lev);
		this.typeID = 0xFFFF00;
		this.blocking = false;
		this.baseColor = Art.getColor(255,255,255);
	}
	
	public void init() {
		// Set the tilesheet offset to be the same as either left
		// or right tile ( whichever one is not a blocking tile is selected )
		Tile leftTile = level.getTile(tx-1,ty);
		Tile rightTile = level.getTile(tx+1, ty);
		
		if( !leftTile.blocking ) {
			this.tsetOffsetX = leftTile.tsetOffsetX;
			this.tsetOffsetY = leftTile.tsetOffsetY;
		} else {
			this.tsetOffsetX = rightTile.tsetOffsetX;
			this.tsetOffsetY = rightTile.tsetOffsetY;
		}			
	}
}
