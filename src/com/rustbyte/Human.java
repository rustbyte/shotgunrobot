package com.rustbyte;

import java.util.List;

import com.rustbyte.PathFinder.Node;
import com.rustbyte.level.RescueZoneTile;
import com.rustbyte.level.Tile;
import com.rustbyte.vector.Vector2;

public class Human extends Mob {
	private int ANIM_WALK_RIGHT;
	private int ANIM_WALK_LEFT;
	private int ANIM_IDLE_LEFT;
	private int ANIM_IDLE_RIGHT;	
	private int actionTimer = 0;
	private boolean followingPlayer = false;
	private Entity currentTarget = null;
	private int foundPlayerTimer = 0;
	private int minSpacingToPlayer = 0;
	private int maxSpacingToPlayer = 0;
	
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
		flashEffectPlayerFound = new FlashEffect(0x00FF00, 4, w,h);
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
				
		if(hitpoints <= 0) {
			this.breakApart(16, Art.getColor(255,0,0), 10);
			alive = false;
			game.humansLost++;
		}
		
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
			
			Tile currentTile = game.level.getTileFromPoint(xx,yy);
			
			// Try to find that handsome hero.....
			if( currentTarget == null) {										
				
				// "Sence" things two tiles behind, and "see" things 4 tiles infront.
				int txStart = currentTile.tx + ( (-facing) * 2);
				int tyStart = currentTile.ty;
				for(int i=0; i < 7 && currentTarget == null; i++) {
					Tile nextTile = game.level.getTile(txStart + (facing * i), tyStart);
					if( nextTile == null || nextTile.blocking) break;
					
					List<Entity> ents = nextTile.getEntities();
					for(int j=0; j < ents.size();j++) {
						Entity nextEntity = ents.get(j);
						if(  nextEntity instanceof Player) {
							
							// Atlast! Rescued! Thank god you're here!
							// WAIT! DONT SHOOT! Im with the science team!!!
							//System.out.println("I found a hero!!!.");
							currentTarget = nextEntity;
							followingPlayer = true;
							foundPlayerTimer = 200;
							minSpacingToPlayer = 1 + rand.nextInt(5);
							maxSpacingToPlayer = minSpacingToPlayer + 10 + rand.nextInt(20);
							
							game.addEntity(new FloatingText("GET ME OUT!",
									Art.getColor(255,255,0),xx - 80,yy - 20,
									new Vector2(0,-1), null, game));
							
							break;
						}
					}
				}
			} else {
				
				// Is there an exit near here?
				Tile nextTile = game.level.getTile( currentTile.tx, currentTile.ty);
				if( !(nextTile instanceof RescueZoneTile) ) {					
					
					// Gona try and stay close to this guy. So tall, and kind of handsome...
					Vector2 v1 = new Vector2(xx,yy);
					Vector2 v2 = new Vector2(currentTarget.xx, currentTarget.yy);
					Vector2 v3 = v1.sub(v2);
					double dist = v3.length();
					if(dist > minSpacingToPlayer &&
					   dist < maxSpacingToPlayer) {
						System.out.println("Stopping");
						dirX = 0;
					} else if(dist < (minSpacingToPlayer - 10)) {
						System.out.println("Im to close!");
						dirX = currentTarget.xx < xx ? 1 : -1; 	// Dont get to close.
					} else if(dist > maxSpacingToPlayer && dist < 90) {
						System.out.println("Moving closer");
						dirX = currentTarget.xx < xx ? -1 : 1; 	// Moving a bit closer
					} else if(dist > 100 ) {
						// He left me!!! That bastard left me here!!
						//System.out.println("Hero is to far away!");
						currentTarget = null;
					}
				} else {
					// Found an exit!
					this.explode(16, Art.getColor(0,255,0), 10);
					this.alive = false;
					game.humansSaved++;
					game.addEntity( new FloatingText("SAVED!",
								    Art.getColor(0,255,0), xx, yy, new Vector2(0,-1), null, game));
					
					rand.setSeed(game.tickcount);
					
					double throwVel = -1;
					for(int i=0; i < 5 + (rand.nextInt(3)); i++) {
						
						Powerup p = Powerup.createPowerup(1 + rand.nextInt(3), (int)xx, (int)yy - 20, null, game);
						p.velocity.x = (throwVel * (1 + (rand.nextInt(2) * 0.1)));
						p.velocity.y = -(2.0 + ((double)rand.nextInt(3)));
						throwVel = -throwVel;
						game.addEntity( p );
					}
					
				}
			}
			
			if((currentTarget == null || !currentTarget.alive) && followingPlayer) {
				//System.out.println("The hero left me here to die!!!!");
				currentTarget = null;
				followingPlayer = false;
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
			velocity.x = dirX * (speed * (currentTarget != null ? 2 : 1)); 				
		}
		
		move();

		if(!knockedBack) {
			if(velocity.x < 0 ) this.animator.setCurrentAnimation(ANIM_WALK_LEFT);
			else if(velocity.x > 0) this.animator.setCurrentAnimation(ANIM_WALK_RIGHT);
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
}
