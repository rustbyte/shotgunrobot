package com.rustbyte;

import java.util.List;

import com.rustbyte.PathFinder.Node;
import com.rustbyte.level.Tile;
import com.rustbyte.vector.Vector2;

public class Human extends Mob {
	private int ANIM_WALK_RIGHT;
	private int ANIM_WALK_LEFT;
	private int ANIM_IDLE_LEFT;
	private int ANIM_IDLE_RIGHT;	
	private int actionTimer = 0;
	private boolean followingPlayer = false;
	private boolean foundPlayer = false;
	private int foundPlayerTimer = 0;
	private FlashEffect flashEffectPlayerFound = null;
	private List<Node> path;
	
	public Human(int x, int y, int w, int h, Entity p, Game g) {
		super(x, y, w, h, p, g);
		
		ANIM_WALK_RIGHT = this.animator.addAnimation(5, 121, 122, w, h, false,1);
		ANIM_WALK_LEFT = this.animator.addAnimation(5, 121, 122, w, h, true,1);
		ANIM_IDLE_RIGHT = this.animator.addAnimation(1, 100, 122, w, h, false,1);
		ANIM_IDLE_LEFT = this.animator.addAnimation(1, 100, 122, w, h, true,1);
		
		animator.setCurrentAnimation(ANIM_WALK_RIGHT);
		
		hitpoints = 100;
		speed = 0.75;
		flashEffectPlayerFound = new FlashEffect(0x00FF00, 3, w,h);
		flashEffect.setColor(0xFFFF00);		
	}	

	private double distanceToPlayer() {
		Vector2 v1 = new Vector2(xx,yy);
		Vector2 v2 = new Vector2(game.player.xx, game.player.yy);				
		return v1.sub(v2).length();		
	}
	
	@Override
	public void tick() {
		super.tick();
				
		if(hurtTimer <= 0)
			knockedBack = false;	
		
		if(!knockedBack) {			
			if(--actionTimer <= 0) {
				if(!followingPlayer) {
					int tempNewDir = 0;
					rand.setSeed(System.nanoTime());
					switch(1 + rand.nextInt(3)) {
					case 1:
						tempNewDir = 0;
						break;
					case 2:
						tempNewDir = -1;
						break;
					case 3:
						tempNewDir = 1;
						break;
					}				
					dirX = tempNewDir;
					actionTimer = 100 + rand.nextInt(200);
				} 
			}
			
			if(blockedX && onground) {
				int tx = (int)this.xx / game.level.tileWidth;
				int ty = (int)this.yy / game.level.tileHeight;
				boolean jumpObstacle = false;
				for(int i=0; i < 2; i++) {
					Tile t = game.level.getTile(tx + dirX, ty - (i+1));
					if( t == null || !t.blocking) {						
						jump();
						jumpObstacle = true;
						break;
					}
				}
				if(!jumpObstacle) {					
					dirX = -dirX;
				}
			}
			velX = dirX * speed;				
		}
		
		move();

		if(!knockedBack) {
			if(velX < 0 ) this.animator.setCurrentAnimation(ANIM_WALK_LEFT);
			else if(velX > 0) this.animator.setCurrentAnimation(ANIM_WALK_RIGHT);
			else {
				if(facing == -1) this.animator.setCurrentAnimation(ANIM_IDLE_LEFT);
				if(facing == 1) this.animator.setCurrentAnimation(ANIM_IDLE_RIGHT);
			}
		}
		
		animator.tick();
	}
	
	@Override
	public void takeDamage(Entity source, int amount) {
		if(!isHurt()) {
			hitpoints -= amount;
			hurt(50);		
			game.addEntity(new FloatingText("-" + amount,Art.getColor(255,0,0),xx,yy,new Vector2(0,-1), null, game));
			double force = 1.0;
			this.knockBack((source.xx - xx), force);
		}
		
		if(hitpoints <= 0) {
			this.breakApart(16, Art.getColor(255,0,0), 10);
			alive = false;
			game.humansLost++;
		}		
	}

	@Override
	public void render() {
		if(foundPlayerTimer > 0) {
			--foundPlayerTimer;
			flashEffectPlayerFound.clear();
			animator.render(flashEffectPlayerFound.renderFrame, 0, 0);
			flashEffectPlayerFound.render(game.tickcount, game.screen, (((int)xx) - (wid / 2)) - game.level.viewX, 
				   	  					    			    (((int)yy) - (hgt / 2)) - game.level.viewY);			 			
		} else {
			animator.render(game.screen, ((int)xx - wid / 2) - game.level.viewX,
					 					 ((int)yy - hgt / 2) - game.level.viewY);
		}
		
		if(isHurt()) {
			flashEffect.clear();
			animator.render(flashEffect.renderFrame, 0, 0);
			flashEffect.render(game.tickcount, game.screen, (((int)xx) - (wid / 2)) - game.level.viewX,
															(((int)yy) - (hgt / 2)) - game.level.viewY);
		} else {
			animator.render(game.screen, (((int)xx) - (wid / 2)) - game.level.viewX, 
				   	 					 (((int)yy) - (hgt / 2)) - game.level.viewY);
		}		
	}
}
