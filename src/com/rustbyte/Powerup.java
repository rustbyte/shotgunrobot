package com.rustbyte;

import com.rustbyte.Entity;

public class Powerup extends Mob {
	public static final int POWERUP_TYPE_BATTERY = 1;
	public static final int POWERUP_TYPE_SHELLS = 2;
	public static final int POWERUP_TYPE_GRENADES = 3;
	
	private int powerupType = 0;
	private int pickupTimer = -1;
	private boolean pickedup = false;
	private int spawnTimer = 60;
	
	public Powerup(int x, int y, int w, int h, int type, Entity p, Game game) {
		super(x,y,w,h, p, game );
		
		this.powerupType = type;
		int animID = 0; 
		
		switch(this.powerupType) {
		case POWERUP_TYPE_BATTERY: animID = animator.addAnimation(1, 0, 43, w,h, false, 1); break;
		case POWERUP_TYPE_SHELLS: animID = animator.addAnimation(1, 21, 43, w,h, false, 1); break;
		case POWERUP_TYPE_GRENADES: animID = animator.addAnimation(1, 21, 64, w,h, false, 1); break;
		}
		
		this.hitpoints = 1;
		this.animator.setCurrentAnimation(animID);
	}
	
	public static Powerup createPowerup(int type, int x, int y, Entity p, Game g) {
		return new Powerup(x, y, 20, 20, type, p, g);
	}
	
	@Override
	public void tick() {
		super.tick();	
		
		if( --spawnTimer < 0 ) spawnTimer = 0;
		
		if(onground) {
			if(velX > 0)
				velX -= 0.02;
			if( velX < 0)
				velX += 0.02;
			
			if(velX > -0.1 && velX < 0.1)
				velX = 0;
		}
		if(pickedup && pickupTimer > 0 ) {
			velY += -0.3;
			yy += velY;
			ignoresGravity = true;
			if(--pickupTimer <= 0) 
				this.alive = false;
		} else {		
			move();
		}
		
		animator.tick();
	}	
	
	@Override
	public void render() throws Exception {
		
		if( pickedup ) {
			flashEffect.clear();
			animator.render(flashEffect.renderFrame, 0, 0);
			flashEffect.render(game.tickcount, game.screen, (((int)xx) - (wid / 2)) - game.level.viewX,
															(((int)yy) - (hgt / 2)) - game.level.viewY);			
		} else {
			animator.render(game.screen, (((int)xx) - (wid / 2)) - game.level.viewX, 
						 				 (((int)yy + 2) - (hgt / 2)) - game.level.viewY);
		}
	}
	
	public void pickup() {	
		if(!pickedup && spawnTimer == 0) {
			switch(this.powerupType) {
			case POWERUP_TYPE_BATTERY: game.player.hitpoints += 20; break;
			case POWERUP_TYPE_SHELLS: game.player.shells += 10;  break;
			case POWERUP_TYPE_GRENADES: game.player.grenades += 5; break;
			}
			
			pickupTimer = 200;
			pickedup = true;
		}
	}

	@Override
	public void takeDamage(Entity source, int amount) { }
}
