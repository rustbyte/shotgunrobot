package com.rustbyte.level;

import com.rustbyte.Art;
import com.rustbyte.Bitmap;
import com.rustbyte.Entity;
import com.rustbyte.FlashEffect;

public class DoorTile extends Tile {
	private int direction = 1; // 1=EAST,2=WEST,3=NORTH,4=SOUTH
	private boolean isInside = true; // If both adjecent tiles are Empty(outdoor tx-1 and tx+1) tiles, 
									  // isInside is false
	private FlashEffect flashEffect;
	
	public DoorTile(int x, int y, int wid, int hgt, Level lev) {
		super(x, y, wid, hgt, lev);	
		this.typeID = 0x7F6A00;
		this.blocking = true;
		this.baseColor = 0x7F6A00;
		this.tsetOffsetX = 51;
		this.tsetOffsetY = 22;
		this.flashEffect = new FlashEffect(0xFFFFFF, 5, wid,hgt);
	}
	@Override
	public void init() {
		if(tx > 0) {
			if( this.level.getTile(tx-1, ty) instanceof EmptyTile ) {				
				direction = 1;
				isInside = !(this.level.getTile(tx+1,ty) instanceof EmptyTile);
			}
			if( this.level.getTile(tx+1,ty) instanceof EmptyTile )
				direction = 2;
		}
	}
	@Override
	public void tick() {
		if(hurtTimer > 0)
			hurtTimer--;
	}
	@Override
	public void draw(Bitmap dest, int xx, int yy) {
		
		if(isInside)
			Art.sprites.draw(dest, xx, yy, 72, 1, width, height);
		
		if(hurtTimer > 0) {
			flashEffect.clear();
			Art.sprites.draw(flashEffect.renderFrame, 0,0, tsetOffsetX, tsetOffsetY, width, height, (direction == 2));
			flashEffect.render(level.game.tickcount, dest, xx,yy);
		} else {			
			Art.sprites.draw(dest, xx, yy, tsetOffsetX, tsetOffsetY, width, height, (direction == 2));
		}
	}
	@Override
	public void takeDamage(Entity source, int amount) {
		if(hurtTimer <= 0) {
			this.hitpoints -= amount;
			if(hitpoints <= 0) {
				this.tsetOffsetX = 72;
				this.tsetOffsetY = 22;		
				this.blocking = false;
			} else {			
				this.hurtTimer = 50;
			}
		}
	}
}
