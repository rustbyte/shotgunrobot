package com.rustbyte;

import java.util.Random;

import com.rustbyte.Game;
import com.rustbyte.level.Tile;
import com.rustbyte.vector.Vector2;

public abstract class Mob extends Entity implements Destructable {	
	public int hitpoints;	
	protected int hurtTimer = 0;	
	
	public boolean jumping = false;
	public double jumpVel = -4.45;	
	
	protected boolean blockedX = false;
	protected boolean blockedY = false;
	
	protected Random rand = new Random();
	protected FlashEffect flashEffect;
	
	public Mob(double x, double y, int w, int h, Entity p, Game g) {
		super(x, y, w, h, p, g);
		flashEffect = new FlashEffect(0xFFFFFF, 5, w,h);
	}

	public static Mob createMob(String mobType, int x, int y, int w, int h, Entity ent, Game game) {
		
		Mob result = null;
		switch(mobType) {
		case "HUMAN": result = new Human(x,y,w,h,ent,game); break;
		case "ZOMBIE": result = new Zombie(x,y,w,h,ent,game); break;
		case "SKELETON": result = new Skeleton(x,y,w,h,ent,game); break;
		}
		return result;
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
		velocity.x = d == 0 ? 0 : -( (d / Math.sqrt(d * d))  * force);		
		dirX = velocity.x < 0 ? -1 : 1;
		velocity.y = -2;
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
			d.velocity.y = -(4 + rand.nextInt(5 + i));
			if(i % 2 == 0)
				d.velocity.x = 1.0 + rand.nextDouble();
			else
				d.velocity.x = -(1.0 + rand.nextDouble());				
			
			game.addEntity(d);
			ParticleEmitter pe = new ParticleEmitter(0, 0, -d.velocity.x, -1.0, 1, particleCount, color, d, game);
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
			d.velocity.y = -(1 + rand.nextInt(2));
			if(i % 2 == 0)
				d.velocity.x = 0.5;
			else
				d.velocity.x = -0.5;				
			
			game.addEntity(d);
			ParticleEmitter pe = new ParticleEmitter(0, 0, -d.velocity.x, -1.0, 1, particleCount, color, d, game);
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
		
		blockedX = !game.level.moveEntity(this, velocity.x, 0);		
		blockedY = !game.level.moveEntity(this, 0, velocity.y);		
		
		if(velocity.y != 0 )
			onground = false;
		if(onground)
			jumping = false;
				
		yy += velocity.y;
		xx += velocity.x;	
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
			velocity.y = jumpVel;
		}
	}
}
