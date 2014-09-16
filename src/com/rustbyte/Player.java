package com.rustbyte;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.awt.event.KeyEvent;

import com.rustbyte.Game;
import com.rustbyte.vector.Vector2;
import com.rustbyte.level.*;

public class Player extends Mob  {
	private int ANIM_WALK_RIGHT;
	private int ANIM_WALK_LEFT;
	private int ANIM_IDLE_LEFT;
	private int ANIM_IDLE_RIGHT;	
	private int ANIM_JUMP_LEFT;
	private int ANIM_JUMP_RIGHT;
	private int ANIM_IDLE_BORED_RIGHT;
	private int ANIM_IDLE_BORED_LEFT;
	
	private InputHandler input = null;
	
	public boolean weaponFired = false;
	public int weaponTimer = 0;
	private final int weaponDelay = 10;
	private final int weaponDamage = 35;
	private int grenadeTimer = 0;
	public int shells = 0;
	public int grenades = 0;	
	private int stompTimer = 0;
	
	public Player(int x, int y, int w, int h, Entity p, Game g)	{
		super(x, y, w, h, p, g);
		ANIM_WALK_RIGHT = this.animator.addAnimation(5, 121, 1, w, h, false,1);
		ANIM_WALK_LEFT = this.animator.addAnimation(5, 121, 1, w, h, true,1);
		ANIM_IDLE_RIGHT = this.animator.addAnimation(5, 200, 72, w, h, false,1);
		ANIM_IDLE_LEFT = this.animator.addAnimation(5, 200, 72, w, h, true,1);		
		ANIM_JUMP_RIGHT = this.animator.addAnimation(1, 163, 1, w, h, false,1);
		ANIM_JUMP_LEFT = this.animator.addAnimation(1, 163, 1, w, h, true,1);
		ANIM_IDLE_BORED_RIGHT = this.animator.addAnimation(4, 100, 43, w, h, false, 1);
		ANIM_IDLE_BORED_LEFT = this.animator.addAnimation(4, 100, 43, w, h, true, 1);
		
		this.animator.setFrameRate(15.0);
		
		animator.setCurrentAnimation(ANIM_IDLE_RIGHT);
		input = g.input;
		speed = 1.75;
		xr = 4;
		flashEffect.setColor(0xFFFF00);
		hitpoints = 100;
	}
	@Override
	public void tick() {
		super.tick();								
				
		if(hitpoints <= 0) {
			this.explode(16, Art.getColor(255,255,0), 100);
			this.alive = false;
		}				
		
		if(hurtTimer < 10)
			knockedBack = false;
		
		if(--grenadeTimer < 0)
			grenadeTimer = 0;
		
		if(--stompTimer < 0)
			stompTimer = 0;
		
		if(!knockedBack) {
			if(onground || jumping)
				dirY = dirX = 0;
			
			if(input.keys[KeyEvent.VK_T].pressed) {
				stomp();
			}
			if(input.keys[KeyEvent.VK_SPACE].pressed)
				jump();		
			if(input.keys[KeyEvent.VK_LEFT].pressed) {
				dirX = -1;
			}
			if(input.keys[KeyEvent.VK_RIGHT].pressed) {
				dirX = 1;
			}		

			if(input.keys[KeyEvent.VK_D].pressed)
				fireWeapon();
			if(input.keys[KeyEvent.VK_G].pressed && !(grenadeTimer > 0)) {
				Grenade g = new Grenade(xx,yy,game);
				g.velocity.x = (facing * 2) + velocity.x;
				g.velocity.y = -3;
				grenadeTimer = 50;
				game.addEntity(g);
			}
			if(input.keys[KeyEvent.VK_E].pressed) {
				Tile t = game.level.getTileFromPoint((xx + ((xr + 16) * facing)), yy);
				t.interact(this);
			}
			
			if(weaponFired && --weaponTimer <= 0)
				weaponFired = false;
			
			List<Entity> ents = game.level.getTileFromPoint(xx,yy).getEntities();
			for(int i=0; i < ents.size(); i++) {
				Entity nextEntity = ents.get(i);
				if(nextEntity != null && nextEntity instanceof Powerup) {
					((Powerup)nextEntity).pickup();
				}
			}
		}					
		
		velocity.x = dirX * speed;
		if(externalForce.x != 0.0)
			velocity = velocity.add(externalForce);
		
		move();
				
		if(!knockedBack) {
			if(input.idleTime > 200 ) {
				if( facing == -1 ) this.animator.setCurrentAnimation(ANIM_IDLE_BORED_LEFT);
				if( facing == 1 ) this.animator.setCurrentAnimation(ANIM_IDLE_BORED_RIGHT);
			} else {		
				if(velocity.x < 0 ) this.animator.setCurrentAnimation(ANIM_WALK_LEFT);
				else if(velocity.x > 0 ) this.animator.setCurrentAnimation(ANIM_WALK_RIGHT);
				else {
					if(facing == -1) this.animator.setCurrentAnimation(ANIM_IDLE_LEFT);
					if(facing == 1) this.animator.setCurrentAnimation(ANIM_IDLE_RIGHT);
				}				
				if(jumping && facing == 1) this.animator.setCurrentAnimation(ANIM_JUMP_RIGHT);
				if(jumping && facing == -1) this.animator.setCurrentAnimation(ANIM_JUMP_LEFT);
			}
		}
		
		animator.tick();				
	}
	
	private void stomp() {
		if(stompTimer == 0) {
			Tile myTile = game.level.getTileFromPoint((int)xx, (int)yy);
			for(int x = myTile.tx-2;x <= myTile.tx+2;x++) {
				for(int y=myTile.ty-1;y <= myTile.ty+1;y++) {
					Tile t = game.level.getTile(x,y);
					if(t != null) {
						List<Entity> ents = t.getEntities();
						for(int i=0; i < ents.size();i++) {
							Entity ent = ents.get(i);
							if(ent != this && !(ent instanceof Human) 
									       && (ent instanceof Mob)) {
								Vector2 pushVector = new Vector2();
								double force = 2.0 + (rand.nextDouble() * (1 * rand.nextInt(4)));
								pushVector.x = ((1 + (Math.signum(ent.xx - xx) * force)));
								pushVector.y = -(1.0 + force);
								ent.applyForce(pushVector);
								ent.knockedBack = true;
								stompTimer = 50;
								((Mob)ent).hurt(80);
								((Mob)ent).hitpoints /= 2;
							}
						}
					}
				}
			}
		}
	}	
	
	private void fireWeapon() {						
		if(!weaponFired) {
			weaponFired = true;
			weaponTimer = weaponDelay;
			
			//SoundSystem.playSound("PISTOL");
			
			boolean bulletHit = false;
			double bulletTravelDistance = 0;
			double cx = 0; // bullet collision point			
			Tile t = game.level.getTileFromPoint(xx,yy);			
			while(t != null && !bulletHit) {												
				if( t != null) {
					if(t.blocking) {
						t.takeDamage(this, weaponDamage);
						
						if((t.tx * t.width) > xx)
							cx = t.tx * t.width;
						else
							cx = (t.tx * t.width) + t.width - 1; 
						bulletHit = true;
					} else {
						ArrayList<Entity> entities = t.getEntities();
						if(entities.size() > 0) {
							Comparator<Entity> comp = new Comparator<Entity>() {
								public int compare(Entity e1, Entity e2) {
									if((e1.xx - xx) > (e2.xx - xx)) return -1;
									if((e1.xx - xx) < (e2.xx - xx)) return +1;
									return 0;
								}
							};
							
							Collections.sort(entities, comp);
							for(int i=0; i < entities.size();i++) {
								Entity ent = entities.get(i);
								if( ent!= this && ent instanceof Mob) {
									if(ent instanceof Human || ent instanceof Powerup)
										break;
									Mob m = (Mob)ent;
									m.takeDamage(this, weaponDamage);
									cx = ent.xx;
									bulletHit = true;
									break;
								}
							}
						}
					}
				}
				// get next tile
				t = game.level.getTile(t.tx + facing, t.ty);
				bulletTravelDistance += 20;
			}
			if(!bulletHit)
				cx = xx + (bulletTravelDistance * facing);
			BulletTrace bt = new BulletTrace(xx + (11 * facing),yy+1, cx, yy+1,0,0,null,game);
			game.addEntity(bt);			
		}
	}
	
	@Override
	public void render() {		
		if(weaponFired && (weaponTimer > weaponDelay - (weaponDelay / 2)) && !isHurt()) {
			if(facing > 0)
				Art.sprites.draw(game.screen, (((int)xx) + wid/2) - game.level.viewX, 
										      (((int)yy)) - 2 - game.level.viewY, 22, 28,18,7,false);
			else
				Art.sprites.draw(game.screen, (((int)xx) - 18 - wid/2)  - game.level.viewX, 
						 					  (((int)yy)) - 2 - game.level.viewY, 22, 28,18,7,true);			
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
	@Override
	public void takeDamage(Entity source, int amount) {		
		if(!isHurt()) {
			hitpoints -= amount;
			hurt(50);		
			game.addEntity(new FloatingText("-" + amount,Art.getColor(255,0,0),xx,yy,new Vector2(0,-1), null, game));
			double force = 10.0;
			//this.knockBack((source.xx - xx), force);
			velocity.x = velocity.y = 0.0;
			this.applyForce( new Vector2( -(1.0 + (Math.signum(source.xx - xx) * force)), -(1 + rand.nextInt(4))));
			knockedBack = true;
		}
	}
}