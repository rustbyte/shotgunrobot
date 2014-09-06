package com.rustbyte;

import java.util.ArrayList;
import java.util.Random;

import com.rustbyte.level.Tile;
import com.rustbyte.vector.Vector2;

public class Grenade extends Entity {
	private int damage = 100;
	private int fuseTimer = 200;
	private boolean detonated = false;	
	
	private int ANIM_UNEXPLODED;
	private int ANIM_EXPLOSION;
	
	private ParticleEmitter sparks = null;
	
	public Grenade(double x, double y, Game game) {
		super(x,y,5,8,null, game);
		ANIM_UNEXPLODED = this.animator.addAnimation(1, 201, 51, wid, hgt, false, 0);
		ANIM_EXPLOSION = this.animator.addAnimation(9, 0, 164, 20, 20, false, 1);
		animator.setCurrentAnimation(ANIM_UNEXPLODED);		
		this.xr = 2;
		this.yr = 3;
		
		this.sparks = new ParticleEmitter(x, y, -1, -1, 4, 50, 0xFFFFFF, this, game);
		game.addEntity(sparks);
	}

	@Override
	public void tick() {
		super.tick();
		
		if(--fuseTimer <= 0 && !detonated) {
			Tile t = game.level.getTileFromPoint(xx,yy);
			int tx = t.tx - 1;
			int ty = t.ty - 1;
			for(int y=0; y < 3; y++) {
				for(int x=0; x < 3; x++) {
					Tile tt = game.level.getTile(tx + x, ty + y);
					if(tt != null) {						
						ArrayList<Entity> ents = tt.getEntities();
						int size = ents.size();
						int start = 0, end = size;
						if( ents.size() > 50) {			
							start = size / 3;
							end = size / 2;
							System.out.println("exploding " + (end - start) + " entities.");
						}
						for(int i=start; i < end; i++) {
							Entity e = ents.get(i);
							if( e instanceof Destructable) {
								Vector2 v1 = new Vector2(xx,yy);
								Vector2 v2 = new Vector2(e.xx,e.yy);
								double d = v1.sub(v2).length();								
								int dmg = damage - (int)d;								
								((Destructable)e).takeDamage(this, dmg);
							}								
						}
					}
				}
			}
			animator.setCurrentAnimation(ANIM_EXPLOSION);
			animator.setFrameRate(30);
			detonated = true;
		}	
		
		move();
		
		animator.tick();
		if(detonated) {
			if(animator.getCurrentAnimation().currentFrame >= animator.getCurrentAnimation().numFrames - 1) {
				alive = false;
			}
		}
	}
	@Override
	public void move() {
		boolean blockedX, blockedY = false;

		double oldVelX = velX;
		
		blockedX = !game.level.moveEntity(this, velX, 0);
		blockedY = !game.level.moveEntity(this, 0, velY);
		
		if(blockedX) 
			velX = -oldVelX * 0.5;
		
		if(velX > 0)
			velX -= 0.02;
		if( velX < 0)
			velX += 0.02;
		
		if(velX > -0.1 && velX < 0.1)
			velX = 0;
		
		if(velY != 0)
			onground = false;
		
		yy += velY;
		xx += velX;
	}

	@Override
	public void render() {
		animator.render(game.screen, ((int)xx - animator.getCurrentAnimation().frameWidth / 2) - game.level.viewX, 
									 ((int)yy - animator.getCurrentAnimation().frameHeight / 2) - game.level.viewY);		
	}	
}
