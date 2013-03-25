package com.rustbyte.level;

public class DoorTile extends Tile {
	public DoorTile(int x, int y, int wid, int hgt, Level lev) {
		super(x, y, wid, hgt, lev);
		
		//  new Tile(0x7F6A00, true, 0x7F6A00, 51, 22 );
		this.typeID = 0x7F6A00;
		this.blocking = true;
		this.baseColor = 0x7F6A00;
		this.tsetOffsetX = 51;
		this.tsetOffsetY = 22;
	}
}
