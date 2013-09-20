package com.rustbyte;

import java.util.Random;

import com.rustbyte.Game;
import com.rustbyte.level.Tile;

public abstract class Mob extends Entity implements Destructable {	
	public int hitpoints;	
	protected int hurtTimer = 0;	
	
	public boolean jumping = false;
	public double jumpVel = -4.45;
	public boolean knockedBack = false;
	
	protected boolean blockedX = false;
	protected boolean blockedY = false;
	
	protected Random rand = new Random();
	protected FlashEffect flashEffect;
	
	public Mob(int x, int y, int w, int h, Entity p, Game g) {
		super(x, y, w, h, p, g);
		flashEffect = new FlashEffect(0xFFFFFF, 5, w,h);
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
		velX = -( (d / Math.sqrt(d * d))  * force);
		dirX = velX < 0 ? -1 : 1;
		velY = -2;
		jumping = true;
		knockedBack = true;		
	}
	protected void explode(int partitions, int color, int particleCount) {
		double xo = xx - (wid / 2);
		double yo = yy - (hgt / 2);
		double x = 0;
		double y = 0;		
		int cells = (int) Math.sqrt(partitions);		
		for(int i=0; i < partitions; i++) {
			int sx = this.animator.getCurrentAnimation().getOffsetX() + ((i % cells) * (wid/cells));
			int sy = this.animator.getCurrentAnimation().getOffsetY() + ((i / cells) * (hgt/cells));
			
			x = xo + (sx - this.animator.getCurrentAnimation().getOffsetX());
			y = yo + (sy - this.animator.getCurrentAnimation().getOffsetY());
			
			boolean flip = this.animator.getCurrentAnimation().flip;
			Debris d = new Debris(x, y, wid/cells, hgt/cells, sx, sy, flip, this, this.game);
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
	protected void breakApart(int partitions, int color, int particleCount) {
		double xo = xx - (wid / 2);
		double yo = yy - (hgt / 2);
		double x = 0;
		double y = 0;		
		int cells = (int) Math.sqrt(partitions);		
		for(int i=0; i < partitions; i++) {
			int sx = this.animator.getCurrentAnimation().getOffsetX() + ((i % cells) * (wid/cells));
			int sy = this.animator.getCurrentAnimation().getOffsetY() + ((i / cells) * (hgt/cells));
			
			x = xo + (sx - this.animator.getCurrentAnimation().getOffsetX());
			y = yo + (sy - this.animator.getCurrentAnimation().getOffsetY());
			
			boolean flip = this.animator.getCurrentAnimation().flip;
			Debris d = new Debris(x, y, wid/cells, hgt/cells, sx, sy, flip, this, this.game);
			d.velY = -(1 + rand.nextInt(2));
			if(i % 2 == 0)
				d.velX = 0.5;
			else
				d.velX = -0.5;				
			
			game.addEntity(d);
			ParticleEmitter pe = new ParticleEmitter(0, 0, -d.velX, -1.0, 1, particleCount, color, d, game);
			game.addEntity(pe);				
		}
	}
	
	protected boolean isHurt() {
		return (hurtTimer > 0);			
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
			int tx = (int)xx / 20;
			int ty = (int)yy / 20;
			Tile t = game.level.getTile(tx,ty - 1);
			if( t != null && t.blocking) {				
				return;
			}
			jumping = true;
			onground = false;
			velY = jumpVel;
		}
	}
}
