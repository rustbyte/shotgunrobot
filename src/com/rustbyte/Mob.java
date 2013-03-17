package com.rustbyte;

import com.rustbyte.Game;

public abstract class Mob extends Entity implements Destructable {

	public Bitmap renderFrame;
	public int hitpoints;
	
	protected int hurtTimer = 0;
	protected int hurtColor = 0xFFFFFF;
	
	public boolean jumping = false;
	public double jumpVel = -4.45;
	
	protected boolean blockedX = false;
	protected boolean blockedY = false;
	
	public Mob(int x, int y, int w, int h, Entity p, Game g) {
		super(x, y, w, h, p, g);
		renderFrame = new Bitmap(w,h);
	}

	@Override
	public void tick() {
		super.tick();
		if(isHurt()) {
			hurtTimer--;
		}
	}
	protected void hurt(int time) {
		hurtTimer = time;
	}
	protected boolean isHurt() {
		return (hurtTimer > 0);			
	}
	protected void renderHurtEffect() {
		if(game.tickcount % 5 == 0) {
			for(int i=0; i < wid * hgt; i++){
				if(renderFrame.pixels[i] != 0xFF5DFF)
					renderFrame.pixels[i] = hurtColor;
			}		
		}
	}
	
	@Override
	public void move() {
		
		blockedX = blockedY = false;
		
		if(dirX < 0) facing = -1;
		if(dirX > 0) facing = 1;
		
		blockedX = !game.level.moveEntity(this, velX, 0);		
		blockedY = !game.level.moveEntity(this, 0, velY);		
		
		if(velY != 0)
			onground = false;
		if(onground)
			jumping = false;
				
		yy += velY;
		xx += velX;	
	}
	
	public void jump() {
		if(!jumping && onground) {
			jumping = true;
			onground = false;
			velY = jumpVel;
		}
	}
}
