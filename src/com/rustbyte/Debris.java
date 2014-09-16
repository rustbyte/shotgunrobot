package com.rustbyte;

import java.util.Random;

public class Debris extends Entity {

	private int ticks;
	private int spriteX;
	private int spriteY;
	private boolean flip;
	protected boolean blockedX = false;
	protected boolean blockedY = false;
	private int lifetime;
	
	private Random rand = new Random();
	
	public Debris(double x, double y, int w, int h, int sx, int sy, boolean flip, Entity p, Game g) {
		super(x, y, w, h, p, g);
		this.spriteX = sx;
		this.spriteY = sy;
		this.flip = flip;
		this.xr = (wid / 2);
		this.yr = (hgt / 2);
		this.ticks = 60 + rand.nextInt(120);
		this.lifetime = rand.nextInt(380);
	}
	
	@Override
	public void tick() {
		super.tick();
		if(--ticks < 0)
			ticks = 0;
		if(--lifetime < 0)
			this.alive = false;
		
		move();		
	}
	
	@Override
	public void render() {
		parent.animator.bitmap.draw(game.screen, (((int)xx) - game.level.viewX) - (wid/2),
									  (((int)yy) - game.level.viewY) - (hgt/2),
									  spriteX, spriteY, wid, hgt, flip);
	}

	@Override
	public void move() {
		blockedX = blockedY = false;
		
		blockedX = !game.level.moveEntity(this, (int)velocity.x, 0);		
		blockedY = !game.level.moveEntity(this, 0, (int)velocity.y);

		if(velocity.y != 0)
			onground = false;
				
		yy += (int)velocity.y;
		if(ticks > 0)
			xx += (int)velocity.x;	
	}

}
