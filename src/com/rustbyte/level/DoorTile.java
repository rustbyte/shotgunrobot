package com.rustbyte.level;

import com.rustbyte.Art;
import com.rustbyte.Bitmap;
import com.rustbyte.Entity;
import com.rustbyte.FlashEffect;

public class DoorTile extends Tile {
	protected int direction = 1; // 1=EAST,2=WEST,3=NORTH,4=SOUTH
	protected boolean isInside = true; // If both adjecent tiles are Empty(outdoor tx-1 and tx+1) tiles, 
									  // isInside is false
	protected boolean isOpen = false;
	protected boolean isBroken = false;
	private int interactTimer = 0;
	private int openTileX = 22;
	private int openTileY = 22;
	private int closedTileX = 1;
	private int closedTileY = 22;
	private int backgroundTileX;
	private int backgroundTileY;
	
	private FlashEffect flashEffect;
	
	public DoorTile(int x, int y, int wid, int hgt, Level lev) {
		super(x, y, wid, hgt, lev);	
		this.typeID = 0x7F6A00;
		this.blocking = true;
		this.baseColor = 0x7F6A00;
		this.tsetOffsetX = 1;
		this.tsetOffsetY = 22;
		this.flashEffect = new FlashEffect(0xFFFFFF, 5, wid,hgt);
	}
	@Override
	public void init() {
		Tile leftTile = this.level.getTile(tx-1, ty);
		Tile rightTile = this.level.getTile(tx+1, ty);
		
		if( (leftTile != null) && leftTile instanceof EmptyTile ) {				
			direction = 1;
			isInside = !(rightTile instanceof EmptyTile);
			backgroundTileX = rightTile.tsetOffsetX;
			backgroundTileY = rightTile.tsetOffsetY;
		} else if( (rightTile != null) && rightTile instanceof EmptyTile ) {
			direction = 2;
			backgroundTileX = leftTile.tsetOffsetX;
			backgroundTileY = leftTile.tsetOffsetY;
		} else if( rightTile != null ) {
			direction = 1;
			backgroundTileX = rightTile.tsetOffsetX;
			backgroundTileY = rightTile.tsetOffsetY;			
		} else if ( leftTile != null ) {
			direction = 1;
			backgroundTileX = leftTile.tsetOffsetX;
			backgroundTileY = leftTile.tsetOffsetY;			
		}
		
	}
	@Override
	public void tick() {
		if(hurtTimer > 0)
			hurtTimer--;
		if(interactTimer > 0)
			interactTimer--;
	}
	
	@Override
	public void interact(Entity e) {
		if(interactTimer == 0 && !isBroken) {
			System.out.println( this.isOpen ? "Closing door" : "Opening door");
			this.isOpen = !this.isOpen;
			this.blocking = !this.isOpen;
			this.tsetOffsetX = this.isOpen ? this.openTileX : this.closedTileX;
			this.tsetOffsetY = this.isOpen ? this.openTileY : this.closedTileY;
			interactTimer = 20;
			
			// TODO:
			// Need to push out any entities that are standing on this tile
			// when door closes to avoid block-teleport.
		}
	}
	@Override
	public void draw(Bitmap dest, int xx, int yy) {
				
		Art.tiles.draw(dest,xx, yy, backgroundTileX, backgroundTileY, width, height);
		
		if(hurtTimer > 0) {
			flashEffect.clear();
			Art.tiles.draw(flashEffect.renderFrame, 0,0, tsetOffsetX, tsetOffsetY, width, height, (direction == 2));
			flashEffect.render(level.game.tickcount, dest, xx,yy);
		} else {			
			Art.tiles.draw(dest, xx, yy, tsetOffsetX, tsetOffsetY, width, height, (direction == 2));
		}
	}
	@Override
	public void takeDamage(Entity source, int amount) {
		if(hurtTimer <= 0) {
			this.hitpoints -= amount;
			if(hitpoints <= 0) {
				this.tsetOffsetX = 43;
				this.tsetOffsetY = 22;		
				this.blocking = false;
				this.isBroken = true;
			} else {			
				this.hurtTimer = 50;
			}
		}
	}
}
