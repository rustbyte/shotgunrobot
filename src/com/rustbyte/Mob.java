package com.rustbyte;

import java.util.Random;

import com.rustbyte.Game;

public abstract class Mob extends Entity implements Destructable {

	public Bitmap renderFrame;
	public int hitpoints;
	
	protected int hurtTimer = 0;
	protected int hurtColor = 0xFFFFFF;
	
	public boolean jumping = false;
	public double jumpVel = -4.45;
	public boolean knockedBack = false;
	
	protected boolean blockedX = false;
	protected boolean blockedY = false;
	
	protected Random rand = new Random();
	
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
	protected void knockBack(double d, double force) {
		velX = -(d / 4) * force;
		dirX = velX < 0 ? -1 : 1;
		velY = -2;
		jumping = true;
		knockedBack = true;		
	}
	protected void explode(int partitions, int color, int particleCount) {
		double x = xx;// - (wid / 2);
		double y = yy;// - (hgt / 2);		
		int psize = partitions / 2;
		for(int i=0; i < partitions; i++) {
			int sx = this.animator.getCurrentAnimation().getOffsetX() + ((i % 2) * (wid/psize));
			int sy = this.animator.getCurrentAnimation().getOffsetY() + ((i / 2) * (hgt/psize));
			boolean flip = this.animator.getCurrentAnimation().flip;
			Debris d = new Debris(x, y, wid/psize, hgt/psize, sx, sy, flip, this, this.game);
			d.velY = -(4 + rand.nextInt(5));
			if(i % 2 == 0)
				d.velX = 1;
			else
				d.velX = -1;				
			
			game.addEntity(d);
			ParticleEmitter pe = new ParticleEmitter(0, 0, -d.velX, -1.0, 1, particleCount, color, d, game);
			game.addEntity(pe);				
		}		
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
